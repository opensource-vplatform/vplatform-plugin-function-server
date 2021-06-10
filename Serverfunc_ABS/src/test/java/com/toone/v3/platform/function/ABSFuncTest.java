package com.toone.v3.platform.function;

import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFuncVObject;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class ABSFuncTest {

    private final ABSFunc func = new ABSFunc();
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
//        double input = -10;
//        IFuncContext context = getIFuncContext(input);
//        IFuncOutputVo outputVo = func.evaluate(context);
//        double out = (double) outputVo.get();
//        System.out.println(out);
//        assertEquals(Math.abs(input), out, 0.0);
    }

    private IFuncContext getIFuncContext(Object input) {
       final Object[] angle = new Object[1];
        angle[0] = input;

        return new IFuncContext() {
            @Override
            public Object getInput(int idx) {
                return angle[idx];
            }

            @Override
            public IFuncVObject getVObject() {
                return null;
            }

            @Override
            public int getInputSize() {
                return angle.length;
            }

            @Override
            public IFuncOutputVo newOutputVo() {
                return new IFuncOutputVo() {
                    @Override
                    public IFuncOutputVo put(Object val) {
                        return null;
                    }

                    @Override
                    public Object get(String key) {
                        return null;
                    }

                    @Override
                    public Object get() {
                        return null;
                    }

                    @Override
                    public boolean isSuccess() {
                        return false;
                    }

                    @Override
                    public String getMessage() {
                        return null;
                    }

                    @Override
                    public IFuncOutputVo setSuccess(boolean success) {
                        return null;
                    }

                    @Override
                    public IFuncOutputVo setMessage(String message) {
                        return null;
                    }
                };
            }
        };
    }
}