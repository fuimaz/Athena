package com.fuimaz.athena.pattern;

/**
 * Created by Fuimaz on 2017/6/11.
 */
public abstract class CallChainProcessor<T> {

    protected CallChainProcessor<T> processor;

    public void setProcessor(CallChainProcessor<T> processor) {
        this.processor = processor;
    }

    public T handle(T input) {
        T result = handleWork(input);
        if (processor != null) {
            return processor.handle(result);
        }

        return result;
    }

    abstract protected T handleWork(T input);
}
