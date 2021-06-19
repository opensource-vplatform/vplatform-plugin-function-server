package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpCommand;
import com.yindangu.v3.plugin.vds.reg.api.IRegisterPlugin;
import com.yindangu.v3.plugin.vds.reg.api.builder.IFunctionBuilder;
import com.yindangu.v3.plugin.vds.reg.api.builder.IHttpCommandBuilder;
import com.yindangu.v3.plugin.vds.reg.api.model.*;
import com.yindangu.v3.plugin.vds.reg.common.RegVds;

import java.util.ArrayList;
import java.util.List;

/**
 * 函数注册器
 * 
 * @Author xugang
 * @Date 2021/5/31 21:56
 */
public class GetLngOrLatByAddrRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_GetLngOrLatByAddr";
    private static final String Component_Version = "3.10.0";
    private static final String Command_Code = "GetLngOrLatByAddr";

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
        plugins.add(getCommand());

        return plugins;
    }

    /**
     * GetLngOrLatByAddr函数
     *
     * @return GetLngOrLatByAddr函数描述器
     */
    private IFunctionProfileVo getFunc() {
        IFunctionBuilder pluginBuilder = RegVds.getPlugin().getFunctiontPlugin();
        IFunctionProfileVo.IFunctionInputVo inputVo1 = pluginBuilder.newInput()
                .setDesc("地址")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionInputVo inputVo2 = pluginBuilder.newInput()
                .setDesc("返回类型")
                .setType(VariableType.Char)
                .setRequired(true)
                .build();
        IFunctionProfileVo.IFunctionOutputVo outputVo = pluginBuilder.newOutput()
                .setDesc("返回值")
                .setType(VariableType.Number)
                .build();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.GetLngOrLatByAddr.Function_Code())
                .setDesc(ServerFuncCommonUtils.GetLngOrLatByAddr.Function_Desc())
                .setName(ServerFuncCommonUtils.GetLngOrLatByAddr.Function_Name())
                .setEntry(GetLngOrLatByAddrFunc.class)
                .setExample("代码示例：GetLngOrLatByAddr(\"广东省珠海市港湾大道科技五路19号\",\"lng\")，取相应地址的经度，若第二个参数为lat，则取纬度。\n" +
                        "参数1：地址（字符串，必填），目前仅限国内地址，地址结构（（省/市/区/街道/门牌号））越完整，地址内容越准确，解析的坐标精度越高。\n" +
                        "参数2：lng或lat，其中lng为取经度，lat为取纬度（字符串，必填）\n" +
                        "返回值为数字类型\n" +
                        "注：使用此函数前，需要在业务系统控制台配置相关参数\n" +
                        "1.前往控制台（system/console） -- 系统维护 -- 配置管理 -- 百度鹰眼服务 配置ak\n" +
                        "2.ak的获取：前往百度开放平台创建应用后获取，注意在创建时【应用类型】需选【服务端】（http://lbsyun.baidu.com/apiconsole/key） ")
                .setOutput(outputVo)
                .addInputParam(inputVo1)
                .addInputParam(inputVo2);

        return pluginBuilder.build();
    }

    private IHttpCommandProfileVo getCommand() {
        IHttpCommandBuilder pluginBuilder = RegVds.getPlugin().getHttpCommandPlugin();
        pluginBuilder.setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(Command_Code)
                .setDesc("")
                .setName("获取经纬度")
                .setEntry(GetLngOrLatByAddrCommand.class);

        return pluginBuilder.build();
    }
}
