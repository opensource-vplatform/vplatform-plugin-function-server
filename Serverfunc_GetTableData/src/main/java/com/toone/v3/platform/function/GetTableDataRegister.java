package com.toone.v3.platform.function;

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
 * @Date 2021/5/31 21:59
 */
public class GetTableDataRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetTableData";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "GetTableData";
    private static final String Plugin_Name = "根据表名字段名以及过滤条件获取数据";
    private static final String Plugin_Desc = "根据表名字段名以及过滤条件获取数据,返回获取的结果。";
    private static final String Component_Version = "3.10.0";

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin()
                .getComponentProfile()
                .setGroupId(Group_Id)
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
     * GetTableData函数
     *
     * @return GetTableData函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("列名")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("表名")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("条件")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("结果")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(GetTableDataFunc.class)
                .setExample("代码示例:GetTableData(\"ColumnName\",\"Table1\",\"ID='3'\") 返回值为表Table1中ID=3的行的ColumnName列的值。 \n" +
                        "参数1--表字段名称（字符串类型）； \n" +
                        "参数2--表名称（字符串类型）； \n" +
                        "参数3--过滤条件（字符串类型）； \n" +
                        "若参数3中的条件值来源变量，则参数3需要用一个变量代替，如：\n" +
                        "GetTableData(\"ColumnName\",\"Table1,BR_VAR_PARENT.cs2)\n" +
                        "其中BR_VAR_PARENT.cs2的值=ConcatStr(\"ID==\",BR_VAR_PARENT.cs2_z)\n" +
                        "若BR_VAR_PARENT.cs2_z为字符串，拼接参数3时，还需要加上单引号，如：\n" +
                        "ConcatStr(\"ID==\",\"'\",BR_VAR_PARENT.cs2_z,\"'\")\n" +
                        "返回值为字符串。 \n" +
                        "注：筛选条件中的字符串需要用单引号引起来。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3);

        return pluginBuilder.build();
    }
}
