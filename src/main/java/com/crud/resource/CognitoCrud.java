package com.crud.resource;
import com.amazonaws.services.cognitoidp.model.*;
import com.crud.model.ImageUpload;
        import com.crud.model.User;
        import com.crud.awsresources.AwsConfig;
        import com.crud.awsresources.Cognito;
        import com.crud.exception.InvalidRequestException;
        import com.crud.model.*;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.web.bind.annotation.*;
        import java.util.*;

@RestController
public class CognitoCrud {
    private static Logger logger = LoggerFactory.getLogger(CognitoCrud.class);

    @Autowired
    AwsConfig awsConfig;

    @Autowired
    Cognito cognitoInstance;

    @RequestMapping(path = "/user", method = RequestMethod.POST)
    private SignUpResult saveUser(@RequestBody User users)
    {
        logger.info("Request for Cognito User Registration");
        SignUpResult response = null;
        UUID uuid= UUID.randomUUID();
        logger.debug("Cognito user id: " + uuid);
            try {
                logger.debug("Updating details to cognito...");
                response = cognitoInstance.signUp(uuid.toString(), users.getPassword(), users.getEmail(), users.getPhonenumber());
                logger.debug("Cognito response: " + response);
                return  response;
            }catch (Exception ex){
                    throw ex;
                }
    }

    @RequestMapping(path = "/get_cognito_user_details", method = RequestMethod.POST)
    public GetUserResult getCognitoDetails( @RequestHeader(value="accessToken") String access_token){
        long timeIn = System.currentTimeMillis();
        logger.info("Request for Get Cognito User Details " + access_token.length());
        GetUserResult response = null;
        response = cognitoInstance.authorize(access_token);
        logger.debug("getCognitoDetails response: "+ response);
        return response;
    }

    //resending confirmation link
    @RequestMapping(path = "/resendverifylink", method = RequestMethod.POST)
    public ResendConfirmationCodeResult resendConfirmation( @RequestHeader(value="cognitoid") String cognitoid)  {
        logger.info("Request for resendverifylink " );
        ResendConfirmationCodeResult response = null;
        response = cognitoInstance.resendConfirmation(cognitoid);
        logger.debug("resendverifylink response: "+ response.getSdkHttpMetadata().getHttpStatusCode());
        return response;
    }

    //after login you will get idtoken, accesstoken and refresh token
    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public InitiateAuthResult login(@RequestBody User requser){
        long timeIn = System.currentTimeMillis();
        logger.info("Request for Login ");
        InitiateAuthResult response = null;
        response = cognitoInstance.userLogin(requser.getEmail(), requser.getPassword());
        logger.debug("Login response Status: " + response.getSdkHttpMetadata().getHttpStatusCode());
        return response;
    }

    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public AdminUserGlobalSignOutResult logout(@RequestHeader(value="accessToken") String access_token){
        logger.info("Request for Logout "+access_token.length());
        String cognitousername = null;
        AdminUserGlobalSignOutResult response = null;
        cognitousername = cognitoInstance.authorize(access_token).getUsername();
        response = cognitoInstance.userLogoutadmin(cognitousername);
        logger.debug("Logout response Status: " + response.getSdkHttpMetadata().getHttpStatusCode());
        return response;
    }

    //using refresh token, we can get new idtoken and accesstoken
    @RequestMapping(path = "/refresh_token", method = RequestMethod.POST)
    public InitiateAuthResult refreshToken(@RequestHeader(value="refresh_token") String  refresh_token){
        logger.info("Request for refresh Token "+ refresh_token.length() );
            InitiateAuthResult	response = cognitoInstance.genrateNewToken(refresh_token);
            logger.debug("Response: "+ response.getAuthenticationResult());
            return response;
    }

//verification code will be sent on registered email or phone no.
    @RequestMapping(path = "/getuserattributes", method = RequestMethod.POST)
    public GetUserAttributeVerificationCodeResult verifyPhone(@RequestBody ResetPassword resetPassword, @RequestHeader(value="accessToken") String access_token){
        long timeIn = System.currentTimeMillis();
        logger.info("Request for verify Phone or Email" + access_token.length());
        GetUserAttributeVerificationCodeResult response = null;
        if (resetPassword.isPhone_number_verify() == true) {
            response = cognitoInstance.getAttributeVerificationCode("phone_number", access_token);
        }
        if (resetPassword.isEmail_verify() == true) {
            response = cognitoInstance.getAttributeVerificationCode("email", access_token);
        }
        if (resetPassword.isEmail_verify() == false && resetPassword.isPhone_number_verify() == false) {
            throw new InvalidRequestException("Both User Attributes Can't be false");
        }
        return response;
    }

    //for verification code
    @RequestMapping(path = "/verifyuserattributes", method = RequestMethod.POST)
    public VerifyUserAttributeResult verifyAttribute(@RequestBody ResetPassword resetPassword, @RequestHeader(value="accessToken") String access_token) {
        long timeIn = System.currentTimeMillis();
        logger.info("Request for verify Otp "+ access_token.length());
        VerifyUserAttributeResult response = null;
        if (resetPassword.isPhone_number_verify() == true) {
            response = cognitoInstance.verifyAttribute("phone_number", resetPassword.getVerificationCode(), access_token);
        }
        if (resetPassword.isEmail_verify() == true) {
            response = cognitoInstance.verifyAttribute("email", resetPassword.getVerificationCode(), access_token);
        }
        if (resetPassword.isEmail_verify() == false && resetPassword.isPhone_number_verify() == false) {
            throw new InvalidRequestException("Both User Attributes Can't be false");
        }
        return response;
    }

    @RequestMapping(path = "/forget_password", method = RequestMethod.POST)
    public ForgotPasswordResult forgotPassword(@RequestHeader(value="cognitoid") String cognitoid)  {
        logger.info("Request for Forget Password " );
        ForgotPasswordResult response = null;
        response = cognitoInstance.forgotPassword(cognitoid);
        logger.debug("forgot response: "+ response);
        return response;
    }

    @RequestMapping(path = "/confirm_forget_password", method = RequestMethod.POST)
    public ConfirmForgotPasswordResult confirmForgotPassword(@RequestBody ResetPassword requser){
        logger.info("Request for confirm ForgotPassword " );
        ConfirmForgotPasswordResult response = cognitoInstance.confirmForgotPassword(requser.getEmail(),requser.getNewPassword(),requser.getVerificationCode());
        logger.debug("confirmForgotPassword response: "+ response);
        return response;
    }

    @PostMapping("/reset_password")
    public ChangePasswordResult resetPassword(@RequestBody ResetPassword requser, @RequestHeader(value="accessToken") String access_token){
        logger.info("Request for Reset Password ");
        ChangePasswordResult response = null;
        response = cognitoInstance.passwordReset(requser.getOldPassword(), requser.getNewPassword(),access_token);
        return response;
    }

    // updating user attribute such as email, password
    @RequestMapping(path = "/updateUserAttribute", method = RequestMethod.POST)
    public UpdateUserAttributesResult updateUserAttribute(@RequestBody UserAttribute userAttribute, @RequestHeader(value="accessToken") String access_token){
        logger.info("Request for Update User Attribute "+ access_token.length());
        UpdateUserAttributesResult response = null;
        List<AttributeType> list = new ArrayList<>();
        AttributeType attributeType = new AttributeType();
        if(userAttribute.isAllowphonenumber()==true){
            attributeType.setName("phone_number");
            attributeType.setValue(userAttribute.getPhonenumber());
        }
        if(userAttribute.isAllowemail()==true){
            attributeType.setName("email");
            attributeType.setValue(userAttribute.getEmail());
        }
        list.add(attributeType);
        response = cognitoInstance.updateUserAttributes(access_token,list);
        logger.debug("updateUserAttribute response: "+ response);
        return response;
    }



    @RequestMapping(path = "/getcognitouserlist", method = RequestMethod.GET)
    public String userListCognito() {
        System.out.println("Request for Get All Cognito User Details " );
        ListUsersResult response=null;
        response = cognitoInstance.userList();
        List<UserType> userTypeList = response.getUsers();
        // loop through them
        for (UserType userType : userTypeList) {
            List<AttributeType> attributeList = userType.getAttributes();
            for (AttributeType attribute : attributeList) {
                String attName = attribute.getName();
                String attValue = attribute.getValue();
                System.out.println(attName + ": " + attValue);
            }
        }
        return "success";
    }


//################ uploading/deleting file to/from S3 bucket within folder name as image #############################################

    @RequestMapping(path = "/fileupload", method = RequestMethod.POST)
    private String  saveFiletoS3bucket(@RequestBody ImageUpload imageUpload)
    {
        logger.info("Request for Storing file "+ imageUpload.getImagename());
        //s3 bucket image folder
        String	imageurl = awsConfig.uploadFile("image", imageUpload.getImagename(), imageUpload.getBase64(), "image/jpeg", ".jpeg");
        return imageurl;
    }

    @RequestMapping(path = "/deletefile", method = RequestMethod.DELETE)
    private String  deleteFile(@RequestHeader(value="fileurl") String fileurl)
    {
        logger.info("Request for deleting file "+ fileurl);
        String	response = awsConfig.deleteFileFromS3Bucket(fileurl);
        return response;
    }

}
