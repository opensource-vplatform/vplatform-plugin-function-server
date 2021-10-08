package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.file.api.model.IAppFileInfo;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.platform.plugin.util.VdsUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载网络文件到本地文件系统。<br>
 * <br>
 * 代码示例：DowdloadFileToFileSystem("http://img07.tooopen.com/images/20170316/tooopen_sy_201956178977.jpg","jpg")<br>
 * 参数1：网络文件路径（字符串类型）<br>
 * 参数2：文件类型（可不传，不传可能导致无法识别文件类型）（字符串类型）<br>
 * 返回值为字符串类型，成功时返回文件id字符串，失败时返回-1字符串。<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:08
 */
public class DowdloadFileToFileSystemFunc implements IFunction {

    // 函数编码
    private final static String funcCode = DowdloadFileToFileSystemRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(DowdloadFileToFileSystemFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        String type = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            int size = context.getInputSize();
            if (size == 1) {
                param1 = context.getInput(0);
                if(param1 == null) {
                    throw new ServerFuncException("函数【" + funcCode + "】的第1个参数不能为空");
                }
            } else if (size == 2) {
                param1 = context.getInput(0);
                type = (String) context.getInput(1);
            } else {
                throw new ServerFuncException("函数【" + funcCode + "】参数不合法，需要1个或者2个参数，当前参数个数：" + size);
            }

            String fileId = VdsUtils.uuid.generate();
            URL url;
            try {
                url = new URL((String) param1);
                InputStream inputStream = null;
                inputStream = url.openStream();
                IAppFileInfo appFileInfo = VDS.getIntance().newAppFileInfo();
                appFileInfo.setId(fileId);

                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int length = httpConn.getContentLength();
                appFileInfo.setFileSize(length);
                if (type == null || type.toString().trim().equals("")) {
                    InputStream[] inputStreams = copyInputStream(inputStream);
                    type = FileType.getFileTypeByFileInputStream(inputStreams[1]);
                    appFileInfo.setFileType(type);
                    appFileInfo.setOldFileName(fileId + type);
                    appFileInfo.setDataStream(inputStreams[0]);
                } else {
                    if(!type.startsWith(".")){
                        type="."+type;
                    }
                    appFileInfo.setFileType(type);
                    appFileInfo.setOldFileName(fileId + type);
                    appFileInfo.setDataStream(inputStream);
                }
                VDS.getIntance().getFileOperate().saveFileInfo(appFileInfo);
            } catch (Exception e) {
                log.warn("下载网络文件上传到文件服务失败，url=" + param1.toString(), e);
                outputVo.setSuccess(true);
                outputVo.put("-1");
            }

            outputVo.setSuccess(true);
            outputVo.put(fileId);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参值1：" + param1 + "，参数2：" + type + "，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + type, e);
        }
        return outputVo;
    }

    private InputStream[] copyInputStream(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer;
        try {
            buffer = new byte[inputStream.available()];
            int len;
            while ((len = inputStream.read(buffer)) > -1) {
                baos.write(buffer, 0, len);
            }
            baos.flush();
        } catch (IOException e) {
            log.error("[DowdloadFileToFileSystem]函数获取文件类型", e);
        }

        // 打开一个新的输入流
        InputStream input1 = new ByteArrayInputStream(baos.toByteArray());
        InputStream input2 = new ByteArrayInputStream(baos.toByteArray());
        InputStream[] ins = {input1, input2};
        return ins;
    }
}
