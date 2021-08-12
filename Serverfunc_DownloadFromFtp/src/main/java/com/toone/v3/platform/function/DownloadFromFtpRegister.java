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
 * @Date 2021/5/31 21:09
 */
public class DownloadFromFtpRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_DownloadFromFtp";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "DownloadFromFtp";
    private static final String Plugin_Name = "从ftp下载文件";
    private static final String Plugin_Desc = "从ftp下载文件到文件服务中， 返回文件id";
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
     * DownloadFromFtp函数
     *
     * @return DownloadFromFtp函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("ftp文件名")
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
                .setDesc("ftp文件所在路径")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("文件ID")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(DownloadFromFtpFunc.class)
                .setExample("码示例:DownloadFromFtp(\"test.docx\",\"www.url.com\",21,\"user\",\"pwd\",\"ftp/test\")\n" +
                        "参数1--ftp文件名(字符串类型)\n" +
                        "参数2--服务器地址(字符串类型)\n" +
                        "参数3--端口号(整数类型)\n" +
                        "参数4--ftp用户名(字符串类型)\n" +
                        "参数5--ftp密码(字符串类型)\n" +
                        "参数6--ftp文件所在路径(字符串类型)\n" +
                        "返回值为文件ID(字符串类型)")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3)
                .addInputParam(inputVo4)
                .addInputParam(inputVo5)
                .addInputParam(inputVo6);

        return pluginBuilder.build();
    }
}
