package com.toone.v3.platform.function;

import com.toone.v3.platform.function.common.ServerFuncCommonUtils;
import com.toone.v3.platform.function.common.exception.ServerFuncException;
import com.yindangu.v3.business.VDS;
import com.yindangu.v3.business.file.api.model.IAppFileInfo;
import com.yindangu.v3.business.plugin.business.api.func.IFuncContext;
import com.yindangu.v3.business.plugin.business.api.func.IFuncOutputVo;
import com.yindangu.v3.business.plugin.business.api.func.IFunction;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

/**
 * 系统文件上传到ftp<br>
 * <br>
 * 代码示例:UploadToFtp("6b4a71d391adfbd27313e583dc3bd44b","www.url.com",21,"user","pwd","ftp/test","test.docx")<br>
 * 参数1--文件系统中待上传的文件id(字符串类型)<br>
 * 参数2--服务器地址(字符串类型)<br>
 * 参数3--端口号(整数类型)<br>
 * 参数4--ftp用户名(字符串类型)<br>
 * 参数5--ftp密码(字符串类型)<br>
 * 参数6--文件存放路径(字符串类型)<br>
 * 参数7--文件保存名称(字符串类型)<br>
 * 无返回值<br>
 *
 * @Author xugang
 * @Date 2021/6/17 9:59
 */
public class UploadToFtpFunc implements IFunction {

    // 函数编码
    private final String funcCode = ServerFuncCommonUtils.UploadToFtp.Function_Code();
    private final static Logger log = LoggerFactory.getLogger(UploadToFtpFunc.class);

    @Override
    public IFuncOutputVo evaluate(IFuncContext context) {
        IFuncOutputVo outputVo = context.newOutputVo();
        Object param1 = null;
        Object param2 = null;
        Object param3 = null;
        Object param4 = null;
        Object param5 = null;
        Object param6 = null;
        Object param7 = null;
        try {
            ServerFuncCommonUtils service = VDS.getIntance().getService(ServerFuncCommonUtils.class, ServerFuncCommonUtils.OutServer_Code);
            service.checkParamSize(funcCode, context, 7);
            param1 = context.getInput(0);
            param2 = context.getInput(1);
            param3 = context.getInput(2);
            param4 = context.getInput(3);
            param5 = context.getInput(4);
            param6 = context.getInput(5);
            param7 = context.getInput(6);
            service.checkParamNull(funcCode, param1, param2, param3, param4, param5, param6, param7);

            // MongDB待上传的文件id
            String fileId = (String) param1;
            // FTP服务器地址
            String ftpServerAdd = (String) param2;
            // FTP端口号
            Integer ftpPort = (Integer) param3;
            // FTP登录账号
            String ftpUID = (String) param4;
            // FTP登录密码
            String ftpPWD = (String) param5;
            // FTP服务器中文件保存路径
            String ftpFilePath = (String) param6;
            // FTP所保存的文件名
            String ftpFileName = (String) param7;

            //文件上传至ftp服务器
            uploadFile(ftpFilePath, ftpFileName, fileId, ftpServerAdd, ftpPort, ftpUID, ftpPWD);

            outputVo.put(""); // 该函数本没有返回值
            outputVo.setSuccess(true);
        } catch (ServerFuncException e) {
            outputVo.setSuccess(false);
            outputVo.setMessage(e.getMessage());
        } catch (Exception e) {
            outputVo.setSuccess(false);
            outputVo.setMessage("函数【" + funcCode + "】计算有误，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，参数4：" + param4 + "，参数5：" + param5 + "，参数6：" + param6 + "，参数7：" + param7 + ", " + e.getMessage());
            log.error("函数【" + funcCode + "】计算失败，参数1：" + param1 + "，参数2：" + param2 + "，参数3：" + param3 + "，参数4：" + param4 + "，参数5：" + param5 + "，参数6：" + param6 + "，参数7：" + param7, e);
        }
        return outputVo;
    }

    /**
     * 上传文件
     *
     * @param ftpFilePath  ftp服务保存文件夹
     * @param ftpFileName  FTP所保存的文件名
     * @param fileId       待上传文件的id
     * @param ftpServerAdd ftp服务器地址
     * @param ftpPort      端口号
     * @param ftpUID       用户名
     * @param ftpPWD       密码
     * @return
     */
    private boolean uploadFile(String ftpFilePath, String ftpFileName, String fileId, String ftpServerAdd, Integer ftpPort, String ftpUID, String ftpPWD) {
        boolean flag = false;
        InputStream inputStream = null;
        FTPClient ftpClient = null;
        try {
            IAppFileInfo appfileinfo = VDS.getIntance().getFileOperate().getFileInfo(fileId);
            inputStream = appfileinfo.getDataStream();
            ftpClient = initFtpClient(ftpServerAdd, ftpPort, ftpUID, ftpPWD);
            ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
            CreateDirecroty(ftpFilePath, ftpClient);
            //ftpClient.makeDirectory(ftpFilePath);
            ftpClient.changeWorkingDirectory(ftpFilePath);
            ftpClient.storeFile(ftpFileName, inputStream);
            inputStream.close();
            ftpClient.logout();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ftpClient != null && ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 初始化ftp服务器
     */
    private FTPClient initFtpClient(String ftpServerAdd, Integer ftpPort, String ftpUID,
                                    String ftpPWD) {
        FTPClient ftpClient = new FTPClient();
        ftpClient.setControlEncoding("utf-8");
        try {
            ftpClient.connect(ftpServerAdd, ftpPort); // 连接ftp服务器
            ftpClient.login(ftpUID, ftpPWD); // 登录ftp服务器
            int replyCode = ftpClient.getReplyCode(); // 是否成功登录服务器
            if (!FTPReply.isPositiveCompletion(replyCode)) {
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ftpClient;
    }

    // 创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
    private boolean CreateDirecroty(String ftpFilePath, FTPClient ftpClient) throws IOException {
        boolean success = true;
        String directory = ftpFilePath + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/")
                && !changeWorkingDirectory(new String(directory), ftpClient)) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            } else {
                start = 0;
            }
            end = directory.indexOf("/", start);
            String path = "";
            String paths = "";
            while (true) {
                String subDirectory = new String(ftpFilePath.substring(start, end)
                        .getBytes("GBK"), "iso-8859-1");
                path = path + "/" + subDirectory;
                if (!existFile(path, ftpClient)) {
                    if (makeDirectory(subDirectory, ftpClient)) {
                        changeWorkingDirectory(subDirectory, ftpClient);
                    } else {
                        changeWorkingDirectory(subDirectory, ftpClient);
                    }
                } else {
                    changeWorkingDirectory(subDirectory, ftpClient);
                }

                paths = paths + "/" + subDirectory;
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
                if (end <= start) {
                    break;
                }
            }
        }
        return success;
    }

    // 改变目录路径
    private boolean changeWorkingDirectory(String directory, FTPClient ftpClient) {
        boolean flag = true;
        try {
            flag = ftpClient.changeWorkingDirectory(directory);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return flag;
    }

    // 判断ftp服务器文件是否存在
    private boolean existFile(String path, FTPClient ftpClient) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    // 创建目录
    private boolean makeDirectory(String dir, FTPClient ftpClient) {
        boolean flag = true;
        try {
            flag = ftpClient.makeDirectory(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
}
