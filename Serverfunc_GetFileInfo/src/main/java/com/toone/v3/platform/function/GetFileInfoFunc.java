package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.file.api.model.IAppFileInfo;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * 获取文件信息<br>
 * <br>
 * 代码示例：GetFileInfo("8a819ab551c2421c0151c3b597b80ddf","fileName")。<br>
 * 参数1--文件标识(字符串类型)；<br>
 * 参数2--文件信息类型(字符串类型)，其中fileName为文件名，fileSize为文件大小（单位KB），fileType为文件类型扩展名，md5为文件md5码；<br>
 * 返回值为字符串类型。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:51
 */
public class GetFileInfoFunc implements IFunction {

    // 函数编码
    private final static String funcCode = GetFileInfoRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(GetFileInfoFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 2);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            service.checkParamBlank(funcCode, param1, param2);

            IAppFileInfo appFileInfo = VDS.getIntance().getFileOperate().getFileInfo(param1.toString());
            if (appFileInfo != null) {
                String fileType = param2.toString();
                if (fileType.equalsIgnoreCase("FILENAME")) {
                    String fileName = appFileInfo.getOldFileName();
                    outputVo.put(fileName);
                } else if (fileType.equalsIgnoreCase("FILESIZE")) {
                    String fileSize = appFileInfo.getFileSize() / 1024.0 + "";
                    outputVo.put(fileSize);
                } else if (fileType.equalsIgnoreCase("FILETYPE")) {
                    String type = appFileInfo.getFileType();
                    outputVo.put(type);
                } else if (fileType.equalsIgnoreCase("MD5")) {
                    InputStream input = appFileInfo.getDataStream();
                    String md5 = getMD5(input);
                    outputVo.put(md5);
                }
            }

            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + param2 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败", e);
        }
        return outputVo;
    }

    private String getMD5(InputStream in) {
        if (in == null) {
            return null;
        }
        MessageDigest digest;
        byte[] buffer = new byte[8192];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException ignored) {
            }
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }
}
