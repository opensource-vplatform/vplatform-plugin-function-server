package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.yindangu.v3.plugin.vds.reg.api.IRegisterPlugin;
import com.yindangu.v3.plugin.vds.reg.api.builder.IFunctionBuilder;
import com.yindangu.v3.plugin.vds.reg.api.model.IComponentProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.IFunctionProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.IPluginProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.VariableType;
import com.yindangu.v3.plugin.vds.reg.common.RegVds;

import java.util.ArrayList;
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/6/1 17:10
 */
public class UnixtimestampToDateTimeRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_UnixtimestampToDateTime";
    private static final String Component_Version = "3.10.0";

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin()
                .getComponentProfile()
                .setGroupId(ServerFuncCommonUtils.Group_Id)
                .setCode(Component_Code)
                .setVersion(Component_Version)
                .build();
    }

    @Override
    public List<IPluginProfileVo> getPluginProfile() {
        List<IPluginProfileVo> plugins = new ArrayList<>();
        plugins.add(getFunc());

        return plugins;
    }

    /**
     * UnixtimestampToDateTime函数
     *
     * @return UnixtimestampToDateTime函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("时间戳(字符串)")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("格式")
                .setType(VariableType.Char)
                .setRequired(false)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("结果")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.UnixtimestampToDateTime.Function_Code())
                .setDesc(ServerFuncCommonUtils.UnixtimestampToDateTime.Function_Desc())
                .setName(ServerFuncCommonUtils.UnixtimestampToDateTime.Function_Name())
                .setEntry(UnixtimestampToDateTimeFunc.class)
                .setExample("代码示例:UnixtimestampToDateTime(\"1535444053\",\"yyyy-MM-dd HH:mm:ss\"),返回值为:2018-08-28 16:14:13\n" +
                        "参数1--时间戳(字符串类型),单位是秒,必填\n" +
                        "参数2--日期时间格式(字符串类型),格式例如:yyyy-MM-dd HH:mm:ss、yyyy/MM/dd HH:mm:ss、yyyy年MM月dd日 HH时mm分ss秒,默认格式:yyyy-MM-dd HH:mm:ss\n" +
                        "返回值为字符串类型")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
