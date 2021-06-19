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
 * @Date 2021/6/17 9:57
 */
public class TreeDataUpwardCollectRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_TreeDataUpwardCollect";
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
     * TreeDataUpwardCollect函数
     *
     * @return TreeDataUpwardCollect函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("实体编码")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("统计字段编码集合")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("权重字段编码")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo4 = pluginBuilder.newInput()
                .setDesc("树结构配置项")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.TreeDataUpwardCollect.Function_Code())
                .setDesc(ServerFuncCommonUtils.TreeDataUpwardCollect.Function_Desc())
                .setName(ServerFuncCommonUtils.TreeDataUpwardCollect.Function_Name())
                .setEntry(TreeDataUpwardCollectFunc.class)
                .setExample("代码示例:TreeDataUpwardCollect(\"BR_IN_PARENT.workEntity\",\"ca,cb,cc\",\"weight\",\"type:1,pidField:PID,treeCodeField:InnerCode,orderField:orderNo,isLeafField:isLeaf,busiFilterField:busCode\")。\n" +
                        "参数1--实体编码(字符串类型)，方法输入实体（BR_IN_PARENT.entityCode）、方法变量实体（BR_VAR_PARENT.entityCode）、方法输出实体（BR_OUT_PARENT.entityCode）；\n" +
                        "参数2--统计字段编码集合(字符串类型)，如有多个统计字段用,分隔； \n" +
                        "参数3--权重字段编码（字符串类型）；\n" +
                        "参数4--树结构配置项（字符串类型），仅支持层级树结构，其中pidField为父节点字段，treeCodeField为层级码字段，orderField为排序字段，isLeafField为叶子节点字段，busiFilterField为树过滤字段；\n" +
                        "无返回值，函数处理完成后，最终汇总结果保存在传入的实体中。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3)
                .addInputParam(inputVo4);

        return pluginBuilder.build();
    }
}
