@echo off
rem chcp 65001 == utf-8字符集
set setting=D:\m2\settings.xml
echo ==================maven 仓库设置(%setting%)====================
rd /s /Q target
mkdir target 

echo =================批量打包=================
for %%i in (
	Serverfunc_ABS
	Serverfunc_Acos
	Serverfunc_Add
	Serverfunc_AddThousandBits
	Serverfunc_AsciiToUnicode
	Serverfunc_Asin
	Serverfunc_Atan
	Serverfunc_AvgColumnFunc
	Serverfunc_Ceiling
	Serverfunc_ChangeMoneyToChinese
	Serverfunc_CheckCertCodeFun
	Serverfunc_CheckChinese
	Serverfunc_ChineseToUnicode
	Serverfunc_CommonUtils
	Serverfunc_Compare
	Serverfunc_ConcatStr
	Serverfunc_Contains
	Serverfunc_ConvertFunc
	Serverfunc_ConvertPageNumber
	Serverfunc_ConvertVarToEntityColumn
	Serverfunc_CopyFileByFileId
	Serverfunc_Cos
	Serverfunc_Cosh
	Serverfunc_DateAddFunc
	Serverfunc_DateConvert
	Serverfunc_Datediff
	Serverfunc_DateSub
	Serverfunc_DateTimeNow
	Serverfunc_DateTimeToUnixtimestamp
	Serverfunc_DateToString
	Serverfunc_DecodeBASE64
	Serverfunc_DecodeURIComponent
	Serverfunc_DecryptFunc
	Serverfunc_Divide
	Serverfunc_Divrem
	Serverfunc_DowdloadFileToFileSystem
	Serverfunc_DownloadFromFtp
	Serverfunc_E
	Serverfunc_EncodeBASE64
	Serverfunc_EncodeURIComponent
	Serverfunc_EncryptFunc
	Serverfunc_EncryptionFunc
	Serverfunc_EndsWith
	Serverfunc_EvalExpression
	Serverfunc_Exp
	Serverfunc_Floor
	Serverfunc_Format
	Serverfunc_GenerateSequenceNumber
	Serverfunc_GenerateSequenceNumberQuick
	Serverfunc_GenerateUUID
	Serverfunc_GetAgeByIdCard
	Serverfunc_GetAllParentIds
	Serverfunc_GetConditionColumnValue
	Serverfunc_GetDataBaseType
	Serverfunc_GetDateSection
	Serverfunc_GetDistance
	Serverfunc_GetEntityRowCountFunc
	Serverfunc_GetFileInfo
	Serverfunc_GetFileUrlByFileId
	Serverfunc_GetFirstRowColumnValue
	Serverfunc_GetImageUrl
	Serverfunc_GetIPAddressFunc
	Serverfunc_GetLength
	Serverfunc_GetLngOrLatByAddr
	Serverfunc_GetLoactionPlace
	Serverfunc_GetProperty
	Serverfunc_GetSerialNumberFunc
	Serverfunc_GetSystemVariable
	Serverfunc_GetTableData
	Serverfunc_GetZoomLevel
	Serverfunc_HasRecordFunc
	Serverfunc_IndexOf
	Serverfunc_Insert
	Serverfunc_IsEmpty
	Serverfunc_IsNull
	Serverfunc_IsNullOrEmptyFunc
	Serverfunc_IsWhiteOrSpace
	Serverfunc_LastIndexOf
	Serverfunc_ListToStringFunc
	Serverfunc_Log10
	Serverfunc_LogFunc
	Serverfunc_Max
	Serverfunc_MaxColumn
	Serverfunc_MD5Encrypt
	Serverfunc_MD5EncryptByUTF8
	Serverfunc_Min
	Serverfunc_MinColumn
	Serverfunc_Multiply
	Serverfunc_Null
	Serverfunc_NumberCodeAdd
	Serverfunc_PadLeft
	Serverfunc_PadRight
	Serverfunc_Pai
	Serverfunc_Pow
	Serverfunc_Random
	Serverfunc_RecyclingSequenceNumber
	Serverfunc_Remainder
	Serverfunc_Remove
	Serverfunc_ReplaceByIndex
	Serverfunc_ReplaceFunc
	Serverfunc_Round
	Serverfunc_ShortDateNow
	Serverfunc_ShortTimeNow
	Serverfunc_Sign
	Serverfunc_Sin
	Serverfunc_Sinh
	Serverfunc_Sqrt
	Serverfunc_StandardMD5Encrypt
	Serverfunc_StartsWith
	Serverfunc_Substring
	Serverfunc_Subtract
	Serverfunc_Tan
	Serverfunc_Tanh
	Serverfunc_ToLower
	Serverfunc_TotalColumnFunc
	Serverfunc_ToUpper
	Serverfunc_TreeDataUpwardCollect
	Serverfunc_TrimEnd
	Serverfunc_TrimStart
	Serverfunc_Truncate
	Serverfunc_UnicodeToAscii
	Serverfunc_UnicodeToChinese
	Serverfunc_UnixtimestampToDateTime
	Serverfunc_UploadToFtp
	Serverfunc_V3If
	Serverfunc_VConvertEntityToJsonFunc
	Serverfunc_VConvertEntityToXMLFunc
	Serverfunc_VRestoreJsonToEntityFunc
	Serverfunc_VRestoreXMLToEntityFunc
) do (
	echo "处理:===%%i ===" 
	call mvn clean package -f %%i\pom.xml --settings %setting%
    call copy %%i\target\*.jar target\
)
   
del target\*-javadoc.jar
del target\*-sources.jar
pause