package com.photograph_u.listener;

public interface HttpPostListener {
    void finish(String response);

    void error(Exception e);
}
