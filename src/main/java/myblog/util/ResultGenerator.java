package myblog.util;

import org.springframework.util.StringUtils;

/**
 * @author FANG
 */
public class ResultGenerator {
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAIL_MESSAGE = "FAIL";
    private static final int DEFAULT_SUCCESS_CODE = 200;
    private static final int RESULT_CODE_SERVER_ERROR = 500;

    public static Result genSuccessResult(){
        return new Result(DEFAULT_SUCCESS_CODE,DEFAULT_SUCCESS_MESSAGE);
    }

    public static Result genSuccessResult(String message){
        Result result =  new Result();
        result.setMessage(message);
        result.setResultCode(DEFAULT_SUCCESS_CODE);
        return result;
    }

    public static Result<Object> genSuccessResult(Object data){
        Result<Object> result =  new Result<>(DEFAULT_SUCCESS_CODE,DEFAULT_SUCCESS_MESSAGE);
        result.setData(data);
        return result;
    }

    public static Result genFailResult(String message) {
        Result result = new Result();
        result.setResultCode(RESULT_CODE_SERVER_ERROR);
        if (StringUtils.isEmpty(message)) {
            result.setMessage(DEFAULT_FAIL_MESSAGE);
        } else {
            result.setMessage(message);
        }
        return result;
    }

    public static Result genErrorResult(int code, String message) {
        Result result = new Result();
        result.setResultCode(code);
        result.setMessage(message);
        return result;
    }
}
