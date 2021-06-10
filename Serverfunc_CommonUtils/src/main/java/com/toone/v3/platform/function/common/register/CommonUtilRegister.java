package com.toone.v3.platform.function.common.register;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
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

    @Override
    public IComponentProfileVo getComponentProfile() {
        return RegVds.getPlugin()
                .getComponentProfile()
                .setGroupId(ServerFuncCommonUtils.Group_Id)
                .setCode(Component_Code)
                .setVersion("3.0.0")
                .build();
    }

    @Override
    public List<IPluginProfileVo> getPluginProfile() {
        IPluginProfileVo profileVo = RegVds.getPlugin()
                .getOutServicePlugin()
                .setAuthor(ServerFuncCommonUtils.Plugin_Author)
                .setCode(ServerFuncCommonUtils.OutService.Function_Code())
                .setName(ServerFuncCommonUtils.OutService.Function_Name())
                .setDesc(ServerFuncCommonUtils.OutService.Function_Desc())
                .setEntry(ServerFuncCommonUtilsImpl.class)
                .build();

        return Arrays.asList(profileVo);
    }
}
