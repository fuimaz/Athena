package com.fuimaz.athena.jar.pattern.processor;

import com.fuimaz.athena.jar.pattern.CallChainProcessor;

/**
 * Created by Fuimaz on 2017/6/11.
 */
public class ProcessorA extends CallChainProcessor<String> {


    @Override
    protected String handleWork(String input) {
        return input + " add by a";
    }
}
