package org.rodrigez.service;

import org.rodrigez.model.Page;
import org.rodrigez.model.PageForm;

import java.util.Set;

public interface PageLoader {
    void run(PageForm form);

    double getStatusBarPercent();

    void pause();
    void resume();
    void stop();
    Set<Page> getPageSet();
}
