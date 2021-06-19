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
 * @Date 2021/5/31 21:51
 */
public class GetEntityRowCountRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetEntityRowCountFunc";
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
     * GetEntityRowCountFunc函数
     *
     * @return GetEntityRowCountFunc函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("实体编码")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("筛选条件")
                .setType(VariableType.Char)
                .setRequired(false)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("记录数")
                .setType(VariableType.Integer)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.GetEntityRowCountFunc.Function_Code())
                .setDesc(ServerFuncCommonUtils.GetEntityRowCountFunc.Function_Desc())
                .setName(ServerFuncCommonUtils.GetEntityRowCountFunc.Function_Name())
                .setEntry(GetEntityRowCountFunc.class)
                .setExample("代码示例:GetEntityRowCountFunc(\"BR_IN_PARENT.entityCode\",\"wb='A'\")，返回方法输入实体[entityCode]中字段wb的值是A的记录数。 \n" +
                        "参数1：实体编码(字符串类型)。实体可以是方法输入(BR_IN_PARENT.entityCode)、方法输出(BR_OUT_PARENT.entityCode)、方法变量(BR_VAR_PARENT.entityCode)；\n" +
                        "参数2：筛选条件(字符串类型)，其运算结果应该是布尔值。如果该参数省略，则返回实体总记录数；\n" +
                        "若参数2中的条件值来源变量，则参数2需要用一个变量代替，如：\n" +
                        "GetEntityRowCountFunc(\"BR_VAR_PARENT.TableName\",BR_VAR_PARENT.cs2)\n" +
                        "其中BR_VAR_PARENT.cs2的值=ConcatStr(\"name=\",BR_VAR_PARENT.cs2_z)\n" +
                        "若BR_VAR_PARENT.cs2_z为字符串，拼接参数2时，还需要加上单引号，如：\n" +
                        "ConcatStr(\"name=\",\"'\",BR_VAR_PARENT.cs2_z,\"'\")\n" +
                        "返回值类型：整型。 \n" +
                        "注：筛选条件中的字符串需要用单引号引起来。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }
}
