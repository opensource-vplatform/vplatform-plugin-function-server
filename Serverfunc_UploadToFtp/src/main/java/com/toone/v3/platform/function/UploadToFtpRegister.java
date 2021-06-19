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
 * @Date 2021/6/17 9:59
 */
public class UploadToFtpRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_UploadToFtp";
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
     * UploadToFtp函数
     *
     * @return UploadToFtp函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("文件ID")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("服务器地址")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("端口号")
                .setType(VariableType.Integer)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo4 = pluginBuilder.newInput()
                .setDesc("ftp用户名")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo5 = pluginBuilder.newInput()
                .setDesc("ftp密码")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo6 = pluginBuilder.newInput()
                .setDesc("文件存放路径")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo7 = pluginBuilder.newInput()
                .setDesc("文件保存名称")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.UploadToFtp.Function_Code())
                .setDesc(ServerFuncCommonUtils.UploadToFtp.Function_Desc())
                .setName(ServerFuncCommonUtils.UploadToFtp.Function_Name())
                .setEntry(UploadToFtpFunc.class)
                .setExample("代码示例:UploadToFtp(\"6b4a71d391adfbd27313e583dc3bd44b\",\"www.url.com\",21,\"user\",\"pwd\",\"ftp/test\",\"test.docx\")\n" +
                        "参数1--文件系统中待上传的文件id(字符串类型)\n" +
                        "参数2--服务器地址(字符串类型)\n" +
                        "参数3--端口号(整数类型)\n" +
                        "参数4--ftp用户名(字符串类型)\n" +
                        "参数5--ftp密码(字符串类型)\n" +
                        "参数6--文件存放路径(字符串类型)\n" +
                        "参数7--文件保存名称(字符串类型) \n" +
                        "无返回值")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3)
                .addInputParam(inputVo4)
                .addInputParam(inputVo5)
                .addInputParam(inputVo6)
                .addInputParam(inputVo7);

        return pluginBuilder.build();
    }
}
