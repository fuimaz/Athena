package com.fuimaz.athena.jar.pattern;

import com.fuimaz.athena.jar.pattern.processor.ProcessorA;
import com.fuimaz.athena.jar.pattern.processor.ProcessorB;
import com.fuimaz.athena.jar.pattern.processor.ProcessorC;
import junit.framework.TestCase;

import java.util.function.Function;
import java.util.function.UnaryOperator;

/**
 * Created by Fuimaz on 2017/6/11.
 */
public class CallChainProcessorTest extends TestCase {

    public void testHandle() throws Exception {

        String start = "hello";
        UnaryOperator<String> stringUnaryOperator1 = (String text) -> text + " add by 1";
        UnaryOperator<String> stringUnaryOperator2 = (String text) -> text + " add by 2 ";

        ProcessorA processorA = new ProcessorA();
        ProcessorB processorB = new ProcessorB();
        ProcessorC processorC = new ProcessorC();

        processorA.setProcessor(processorB);
        processorB.setProcessor(processorC);

        Function<String, String> function = stringUnaryOperator1.andThen(stringUnaryOperator2);

        System.out.println(stringUnaryOperator1.apply(processorA.handle(start)));
        System.out.println((processorA.handle(stringUnaryOperator1.apply(start))));
        System.out.println(function.apply(processorA.handle(start)));
    }

}