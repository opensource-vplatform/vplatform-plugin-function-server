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
 * @Date 2021/5/31 21:47
 */
public class GetDataBaseTypeRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetDataBaseType";
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
     * GetDataBaseType函数
     *
     * @return GetDataBaseType函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("数据库类型")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.GetDataBaseType.Function_Code())
                .setDesc(ServerFuncCommonUtils.GetDataBaseType.Function_Desc())
                .setName(ServerFuncCommonUtils.GetDataBaseType.Function_Name())
                .setEntry(GetDataBaseTypeFunc.class)
                .setExample("代码示例:GetDataBaseType()，返回执行系统服务连接的数据库类型（返回值枚举：Mssql2000、Mssql、DB2、Oracle、Mysql、H2、Unknow），其中SqlServer 2000以上版本都返回Mssql，若读取不到数据库类型，则返回Unknow。。 \n" +
                        "无参数；\n" +
                        "返回值类型：字符串类型。")
                .setOutput(outputVo);

        return pluginBuilder.build();
    }
}
