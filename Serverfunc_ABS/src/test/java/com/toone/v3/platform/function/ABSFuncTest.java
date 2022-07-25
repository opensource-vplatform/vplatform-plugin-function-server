package com.toone.v3.platform.function;

import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFuncVObject;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class ABSFuncTest {

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue() {
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