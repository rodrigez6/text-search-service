package org.rodrigez.service;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.rodrigez.model.Page;
import org.rodrigez.model.PageForm;
import org.rodrigez.model.PageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class PageLoaderImpl implements PageLoader {

    @Autowired
    private ChromeOptions options;

    private String searchText;
    private int urlCount;
    private int urlMaxCount;

    private Set<Page> pageSet;
    private List<LoadingThread> loadingThreadList;

    public Set<Page> getPageSet(){
        return pageSet;
    }

    public void run(PageForm form){

        String url = form.getUrl();
        Page startPage = new Page(url);
        String searchText = form.getSearchText();
        int urlMaxCount = form.getUrlMaxCount();
        int threadCount = form.getThreadCount();

        pageSet = Collections.synchronizedSet(new LinkedHashSet<>());
        pageSet.add(startPage);

        this.searchText = searchText;
        this.urlCount = urlMaxCount;
        this.urlMaxCount = urlMaxCount;

        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        loadingThreadList = new ArrayList<>(threadCount);
        for(int i=0;i<threadCount;i++){
            loadingThreadList.add(new LoadingThread(options));
        }
        loadingThreadList.forEach(executorService::execute);
    }

    @Override
    public double getStatusBarPercent(){
        return (1-((double)urlCount/ (double)urlMaxCount))*100;
    }

    @Override
    public void pause() {
        loadingThreadList.forEach(thread -> thread.isPaused = true);
    }

    @Override
    public void resume() {
        loadingThreadList.forEach(thread -> thread.isPaused = false);
    }

    @Override
    public void stop() {
        loadingThreadList.forEach((thread -> thread.isStopped = true));
    }

    class LoadingThread implements Runnable{

        WebDriver driver;
        volatile boolean isPaused = false;
        volatile boolean isStopped = false;

        LoadingThread(ChromeOptions options) {
            driver = new ChromeDriver(options);
        }

        @Override
        public void run() {
            while(urlCount>0&&!isStopped){
                if(!isPaused){
                    Page nextPage = next();
                    if(nextPage!=null){
                        urlCount--;
                        loadPage(driver, nextPage);
                    }
                }
            }
        }
    }

    private synchronized Page next(){
        Iterator<Page> iterator = pageSet.iterator();
        while(true){
            Page nextPage;
            if(iterator.hasNext()){
                nextPage = iterator.next();
                if(!nextPage.isProcessed()){
                    return nextPage;
                }
            }else{
                return null;
            }
        }
    }

    private void loadPage(WebDriver driver, Page page){
        String url = page.getUrl();
        page.setStatus(PageStatus.LOADING);
        page.setProcessed(true);
        try {
            driver.get(url);
            int count = addPages(driver);
            page.setRepeatsCount(count);
            findText(driver, page, searchText);
        } catch (Exception e) {
            page.setStatus(PageStatus.ERROR);
            page.setError(e.getLocalizedMessage());
        }
    }

    private int addPages(WebDriver driver){
        int count = 0;
        List<WebElement> elements = driver.findElements(By.tagName("a"));
        for(WebElement element:elements){
            String href = element.getAttribute("href");
            Page newPage = new Page(href);
            boolean result = addPage(newPage);
            if(result){
                count++;
            }
        }
        return count;
    }

    private synchronized boolean addPage(Page page){
        if(pageSet.size()<urlMaxCount&&!pageSet.contains(page)){
            pageSet.add(page);
            return true;
        }
        return false;
    }

    private void findText(WebDriver driver, Page page, String searchText){
        String pageSource = driver.getPageSource();
        if(pageSource.contains(searchText)){
            page.setStatus(PageStatus.FOUND);
        }else {
            page.setStatus(PageStatus.NOT_FOUND);
        }
    }
}