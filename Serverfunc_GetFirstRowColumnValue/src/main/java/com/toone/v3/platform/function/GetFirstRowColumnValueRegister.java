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
import java.util.Arrays;
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/5/31 21:53
 */
public class GetFirstRowColumnValueRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetFirstRowColumnValue";
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
     * GetFirstRowColumnValue函数
     *
     * @return GetFirstRowColumnValue函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("实体名称")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("字段名称")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Range)
                .setTypeRange(Arrays.asList(VariableType.Number, VariableType.LongDate, VariableType.Date, VariableType.Char, VariableType.Text, VariableType.Integer, VariableType.Boolean))
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.GetFirstRowColumnValue.Function_Code())
                .setDesc(ServerFuncCommonUtils.GetFirstRowColumnValue.Function_Desc())
                .setName(ServerFuncCommonUtils.GetFirstRowColumnValue.Function_Name())
                .setEntry(GetFirstRowColumnValueFunc.class)
                .setExample("代码示例:\n" +
                        "界面实体：GetFirstRowColumnValue(\"entity\",\"id\")，返回值为该实体变量首行记录的id字段值。\n" +
                        "活动集实体：GetFirstRowColumnValue(\"BR_VAR_PARENT.interView\",\"id\")\n" +
                        "参数1--实体名称。界面实体为字符串类型，其他变量实体需要加上变量的前缀\n" +
                        "参数2--字段名(字符串类型)；\n" +
                        "返回值类型：与参数二所选字段的类型一致。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
