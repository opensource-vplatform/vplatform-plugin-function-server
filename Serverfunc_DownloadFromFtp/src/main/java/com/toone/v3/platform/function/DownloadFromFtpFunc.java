package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.file.api.model.IAppFileInfo;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import com.yindangu.v3.platform.plugin.util.VdsUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;

/**
 * 从ftp下载文件到文件服务中， 返回文件id<br>
 * <br>
 * 码示例:DownloadFromFtp("test.docx","www.url.com",21,"user","pwd","ftp/test")<br>
 * 参数1--ftp文件名(字符串类型)<br>
 * 参数2--服务器地址(字符串类型)<br>
 * 参数3--端口号(整数类型)<br>
 * 参数4--ftp用户名(字符串类型)<br>
 * 参数5--ftp密码(字符串类型)<br>
 * 参数6--ftp文件所在路径(字符串类型)<br>
 * 返回值为文件ID(字符串类型)<br>
 *
 * @Author xugang
 * @Date 2021/5/31 21:09
 */
public class DownloadFromFtpFunc implements IFunction {

    // 函数编码
    private final static String funcCode = DownloadFromFtpRegister.Plugin_Code;
    private final static Logger log = LoggerFactory.getLogger(DownloadFromFtpFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        Object param4 = null;
        Object param5 = null;
        Object param6 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);

            service.checkParamSize(funcCode, context, 6);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);
            param4 = context.getInput(3);
            param5 = context.getInput(4);
            param6 = context.getInput(5);
            service.checkParamBlank(funcCode, param1, param2, param3, param4, param5);

            // 所要下载的文件名
            String ftpFileName = (String) param1;
            // FTP服务器地址
            String ftpServerAdd = (String) param2;
            // FTP端口号
            Integer ftpPort = (Integer) param3;
            // FTP登录账号
            String ftpUID = (String) param4;
            // FTP登录密码
            String ftpPWD = (String) param5;
            /*
             * // 所下载文件保存路径 String FilePath = (String) args.get(5);
             */
            // 所要下载的文件在ftp中存放的位置
            String ftpFilePath = (String) param6;
            ftpFilePath = ftpFilePath == null ? "" : ftpFilePath.trim();
//            ftpFilePath = new String(ftpFilePath.getBytes("gbk"), FTP.DEFAULT_CONTROL_ENCODING);
//            ftpFileName = new String(ftpFileName.getBytes("gbk"), FTP.DEFAULT_CONTROL_ENCODING);

            String mongDbFileId = downloadFile(ftpServerAdd, ftpPort, ftpUID,
                    ftpPWD, ftpFilePath, ftpFileName);

            outputVo.put(mongDbFileId);
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，参数4：" + param4 + "，参数5：" + param5 + "，参数6：" + param6 + "，" + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，参数4：" + param4 + "，参数5：" + param5 + "，参数6：" + param6, e);
        }
        return outputVo;
    }

    public String downloadFile(String ftpServerAdd, Integer ftpPort,
                               String ftpUID, String ftpPWD, String pathname, String filename) {
        String finallyFileID = "-1";
        OutputStream os = null;
        FTPClient ftpClient = null;
        File localFile = null;
        FileInputStream in = null;
        try {
            ftpClient = initFtpClient(ftpServerAdd, ftpPort, ftpUID, ftpPWD);
            // 切换FTP目录
            ftpClient.changeWorkingDirectory(pathname);
            FTPFile[] ftpFiles = ftpClient.listFiles();

            StringBuffer sb = new StringBuffer();
            for (FTPFile file : ftpFiles) {
                String name = new String(file.getName().getBytes(FTP.DEFAULT_CONTROL_ENCODING), "gbk");
                if (filename.equalsIgnoreCase(name)) {
                    localFile = new File(file.getName());
                    os = new FileOutputStream(localFile);
                    ftpClient.retrieveFile(file.getName(), os);
                    in = new FileInputStream(localFile);
                    IAppFileInfo appFileInfo = VDS.getIntance().newAppFileInfo();
                    String fileId = VdsUtils.uuid.generate();
                    appFileInfo.setId(fileId);
                    appFileInfo.setDataStream(in);
                    appFileInfo.setOldFileName(name);
                    VDS.getIntance().getFileOperate().saveFileInfo(appFileInfo);
                    sb.append("," + fileId);
                    String[] fileList = sb.toString().split(",");
                    finallyFileID = fileList[fileList.length - 1];
                    break;
                }
            }
            ftpClient.logout();
        } catch (Exception e) {
           throw new RuntimeException("下载ftp文件失败：" + e.getMessage(), e);
        } finally {
            if (ftpClient != null && ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
            if(in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if(localFile != null && localFile.exists()) {
                localFile.delete();
            }
        }
        return finallyFileID;
    }

    /**
     * 初始化ftp服务器
     */
    public FTPClient initFtpClient(String ftpServerAdd, Integer ftpPort,
                              String ftpUID, String ftpPWD) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding(FTP.DEFAULT_CONTROL_ENCODING);
        try {
            ftpClient.connect(ftpServerAdd, ftpPort); // 连接ftp服务器
            ftpClient.login(ftpUID, ftpPWD); // 登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
//                throw new ServerFuncException("登录服务器失败！请检查服务器地址、账号以及密码！");
                throw new RuntimeException("登录ftp服务器失败，状态码：" + replyCode);
            }
            return ftpClient;
        } catch (Exception e) {
            throw new RuntimeException("初始化ftp客户端异常：" + e.getMessage(), e);
        }
    }
}
