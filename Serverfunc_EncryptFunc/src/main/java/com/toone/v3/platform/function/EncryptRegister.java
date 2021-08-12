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
 * @Date 2021/5/31 21:17
 */
public class EncryptRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_EncryptFunc";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    public static final String Plugin_Code = "EncryptFunc";
    private static final String Plugin_Name = "对字符串进行加密";
    private static final String Plugin_Desc = "对字符串进行加密。";
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
     * EncryptFunc函数
     *
     * @return EncryptFunc函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("字符串")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("加密算法")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo3 = pluginBuilder.newInput()
                .setDesc("密钥")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("加密后的值")
                .setType(VariableType.Char)
                .build();
        pluginBuilder.setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setDesc(Plugin_Desc)
                .setName(Plugin_Name)
                .setEntry(EncryptFunc.class)
                .setExample("代码示例：EncryptFunc(\"12345\",\"md5\",\"xxxx\")，返回值为对应加密方式加密后的字符串。\n" +
                        " 参数1：需要加密的字符串，字符串类型，必填。默认使用UTF-8编码\n" +
                        " 参数2：加密算法，字符串类型，必填，不区分大小写。支持以下类型：\n" +
                        "        md5：基于RFC 1321。安全性一般，性能高，不可逆，返回32位16进制。主要用于一致性验证、数字签名等\n" +
                        "        SHA-1：基于NIST's FIPS 180-4，安全比md5高，性能比md5慢，不可逆，返回32位16进制。\n" +
                        "        SHA-256：基于NIST's FIPS 180-4，安全比SHA-1高，性能比SHA-1慢，不可逆，返回64位16进制。\n" +
                        "        SHA-384：基于NIST's FIPS 180-4，安全比SHA-256高，性能比SHA-256慢，不可逆，返回96位16进制。\n" +
                        "        SHA-512：基于NIST's FIPS 180-4，安全比SHA-384高，性能比SHA-384慢，不可逆，返回128位16进制。\n" +
                        "        AES：基于PKCS #5。对称加密，可逆，安全性高，是一种区块加密标准。广泛用于金融财务、在线交易等领域。\n" +
                        " 参数3：密钥，字符串类型，必填；当且仅当参数2为AES，请填写正确密钥，其他参数任意填写。\n" +
                        "        AES密钥要求：长度要求为16byte，不足将补0，超过16byte将自动截取。不建议使用中文作为密钥，推荐大小字符以及数字的组合。\n" +
                        " 返回值类型：加密后的字符串，字符串类型。")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2)
                .addInputParam(inputVo3);

        return pluginBuilder.build();
    }
}
