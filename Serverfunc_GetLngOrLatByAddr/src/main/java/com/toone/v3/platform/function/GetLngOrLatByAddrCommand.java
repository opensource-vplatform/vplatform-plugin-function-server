package com.toone.v3.platform.function;

import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpCommand;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpContext;
import com.yindangu.v3.business.plugin.business.api.httpcommand.IHttpResultVo;

/**
 * 由于历史原因，该函数的功能已command的形式对外使用，因此这里也需要兼容这种场景
 *
 * @Author xugang
 * @Date 2021/6/7 16:40
 */
public class GetLngOrLatByAddrCommand implements IHttpCommand {

    @Override
    public IHttpResultVo execute(IHttpContext context) {
        return null;
    }
}
