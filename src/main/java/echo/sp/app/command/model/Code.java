package echo.sp.app.command.model;

/**
 * Description:全局返回码说明
 * 1.返回码位数：长度为4位方便扩展
 * 2.以百为单位，基本能囊括所有的状态
 * 1-99：基本信息提示
 * 100-199：token相关
 * 200-299：数据格式相关的
 * Author: Charles
 * Date  : 2014/12/18
 */
public class Code {
    /**
     * 成功状态码
     */
    public static String SUCCESS="0000";
    public static String SUCCESS_MSG="成功";

    /**
     * 失败
     */
    public static String FAIL="9999";
    public static String FAIL_MSG="失败";

    /**
     * 缺少相关的参数错
     */
    public static String PARAMS_ERROR="0001";
    public static String PARAMS_ERROR_MSG="参数不完整";

    /**
     * token无效
     */
    public static String TOKEN_INVALID="0100";
    public static String TOKEN_INVALID_MSG="token无效";

    /**
     * 数据格式错误
     */
    public static String DATA_FORMAT_INVALID="0200";
    public static String DATA_FORMAT_INVALID_MSG="数据格式错误";

}
