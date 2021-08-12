package com.toone.v3.platform.function.common.register;

import com.toone.v3.platform.function.common.ServerFuncCommonUtilsImpl;
import com.yindangu.v3.plugin.vds.reg.api.IRegisterPlugin;
import com.yindangu.v3.plugin.vds.reg.api.model.IComponentProfileVo;
import com.yindangu.v3.plugin.vds.reg.api.model.IPluginProfileVo;
import com.yindangu.v3.plugin.vds.reg.common.RegVds;

import java.util.Arrays;
import java.util.List;

/**
 * @Author xugang
 * @Date 2021/5/28 14:02
 */
public class CommonUtilRegister implements IRegisterPlugin {

    private static final String Component_Code = "Serverfunc_CommonUtils";
    private final static String Group_Id = "com.toone.v3.platform";
    private final static String Plugin_Author = "同望科技";
    private final static String Plugin_Code = "serverFuncCommonUtils";
    private final static String Plugin_Name = "服务端函数工具类";
    private final static String Plugin_Desc = "服务端函数工具类";

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin()
                .getComponentProfile()
                .setGroupId(Group_Id)
                .setCode(Component_Code)
                .setVersion("3.0.0")
                .build();
    }

    @Override
    public List<IPluginProfileVo> getPluginProfile() {
        IPluginProfileVo profileVo = RegVds.getPlugin()
                .getOutServicePlugin()
                .setAuthor(Plugin_Author)
                .setCode(Plugin_Code)
                .setName(Plugin_Name)
                .setDesc(Plugin_Desc)
                .setEntry(ServerFuncCommonUtilsImpl.class)
                .build();

        return Arrays.asList(profileVo);
    }
}
