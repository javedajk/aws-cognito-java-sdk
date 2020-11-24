package com.crud.awsresources;
   import com.amazonaws.services.cognitoidp.model.*;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.context.annotation.Configuration;
        import java.util.ArrayList;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

@Configuration
public class Cognito {

    @Autowired
    AwsConfig awsConfig;

    public SignUpResult signUp(String username,String password,String email,String phoneno) {
        try{
            SignUpRequest signUpRequest = new SignUpRequest();
            signUpRequest.setClientId(awsConfig.getClientId());
            signUpRequest.setUsername(username);
            signUpRequest.setPassword(password);
            List<AttributeType> list = new ArrayList<>();
            AttributeType attributeType1 = new AttributeType();
            attributeType1.setName("email");
            attributeType1.setValue(email);
            AttributeType attributeType2 = new AttributeType();
            attributeType2.setName("phone_number");
            attributeType2.setValue(phoneno);
            list.add(attributeType1);
            list.add(attributeType2);
            signUpRequest.setUserAttributes(list);
            SignUpResult authResponse = awsConfig.awsCognitoIdentityProvider().signUp(signUpRequest);
            return authResponse;
        }catch (Exception e){
            throw e;
        }

    }

    public AdminUpdateUserAttributesResult adminUpdateUserAttributes(String username,List<AttributeType> list) throws Throwable {
        try{
            AdminUpdateUserAttributesRequest adminUpdateUserAttributesRequest = new AdminUpdateUserAttributesRequest();
            adminUpdateUserAttributesRequest.setUserAttributes(list);
            adminUpdateUserAttributesRequest.setUserPoolId(awsConfig.getUserPoolId());
            adminUpdateUserAttributesRequest.setUsername(username);
            return awsConfig.awsCognitoIdentityProvider().adminUpdateUserAttributes(adminUpdateUserAttributesRequest);
        }catch (Throwable e){
            throw e;
        }
    }


    public UpdateUserAttributesResult updateUserAttributes(String accessToken,List<AttributeType> list)  {
        try{
            UpdateUserAttributesRequest updateUserAttributesRequest = new UpdateUserAttributesRequest();
            updateUserAttributesRequest.setUserAttributes(list);
            updateUserAttributesRequest.setAccessToken(accessToken);
            return awsConfig.awsCognitoIdentityProvider().updateUserAttributes(updateUserAttributesRequest);
        }catch (Exception e){
            throw e;
        }
    }

    public AdminInitiateAuthResult userLoginadmin(String username,String password) {
        try{
            Map<String,String> authParams = new HashMap<String,String>();
            authParams.put("USERNAME", username);
            authParams.put("PASSWORD", password);
            AdminInitiateAuthRequest authRequest = new AdminInitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
                    .withAuthParameters(authParams)
                    .withClientId(awsConfig.getClientId())
                    .withUserPoolId(awsConfig.getUserPoolId());
            AdminInitiateAuthResult authResponse = awsConfig.awsCognitoIdentityProvider().adminInitiateAuth(authRequest);
            return authResponse;
        }catch (Exception e){
            throw e;
        }
    }

    public AdminUserGlobalSignOutResult userLogoutadmin(String accessToken) {
        try{
            AdminUserGlobalSignOutRequest signoutRequest = new AdminUserGlobalSignOutRequest()
                    .withUsername(accessToken)
                    .withUserPoolId(awsConfig.getUserPoolId());
            AdminUserGlobalSignOutResult signoutResponse = awsConfig.awsCognitoIdentityProvider().adminUserGlobalSignOut(signoutRequest);
            return signoutResponse;
        }catch (Exception e){
            throw e;
        }
    }

    public InitiateAuthResult userLogin(String username,String password) {
        try{
            Map<String,String> authParams = new HashMap<String,String>();
            authParams.put("USERNAME", username);
            authParams.put("PASSWORD", password);
            InitiateAuthRequest authRequest = new InitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .withAuthParameters(authParams)
                    .withClientId(awsConfig.getClientId());
           InitiateAuthResult authResponse = awsConfig.awsCognitoIdentityProvider().initiateAuth(authRequest);
         //   InitiateAuthResult authResponse = awsConfig.awsCognitoIdentityProvider.awsConfig.awsCognitoIdentityProvider()().initiateAuth(authRequest);
            return authResponse;
        }catch (Exception e){
            throw e;
        }
    }

    public GlobalSignOutResult userLogout(String accessToken) {
        try{
            GlobalSignOutRequest signoutRequest = new GlobalSignOutRequest().withAccessToken(accessToken);
            GlobalSignOutResult signoutResponse = awsConfig.awsCognitoIdentityProvider().globalSignOut(signoutRequest);
            return signoutResponse;
        }catch (Exception e){
            throw e;
        }
    }

    public InitiateAuthResult genrateNewToken(String  token) {
        try{
            Map<String,String> authParams = new HashMap<String,String>();
            authParams.put("REFRESH_TOKEN", token);
            InitiateAuthRequest authRequest = new InitiateAuthRequest()
                    .withAuthFlow(AuthFlowType.REFRESH_TOKEN_AUTH)
                    .withAuthParameters(authParams)
                    .withClientId(awsConfig.getClientId());
            InitiateAuthResult authResponse = awsConfig.awsCognitoIdentityProvider().initiateAuth(authRequest);
            return authResponse;
        }catch (Exception e){
            throw e;
        }
    }

    public ListUsersResult userList() {
        try{ListUsersRequest listUsersRequest = new ListUsersRequest();
            listUsersRequest.withUserPoolId(awsConfig.getUserPoolId());
            ListUsersResult result = awsConfig.awsCognitoIdentityProvider().listUsers(listUsersRequest);
            List<UserType> userTypeList = result.getUsers();
            return result;
        }catch (Exception e){
            throw e;
        }
    }

    public AuthenticationResultType confirmUser(String tempPassword,String password,String username){
        Map<String,String> initialParams = new HashMap<String,String>();
        initialParams.put("USERNAME", username);
        initialParams.put("PASSWORD", tempPassword);
        AdminInitiateAuthRequest initialRequest = new AdminInitiateAuthRequest()
                .withAuthParameters(initialParams)
               .withAuthFlow(AuthFlowType.ADMIN_NO_SRP_AUTH)
             //   .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
                .withClientId(awsConfig.getClientId())
                .withUserPoolId(awsConfig.getUserPoolId());
        AdminInitiateAuthResult initialResponse = awsConfig.awsCognitoIdentityProvider().adminInitiateAuth(initialRequest);
        Map<String,String> challengeResponses = new HashMap<String,String>();
        challengeResponses.put("USERNAME", username);
        challengeResponses.put("PASSWORD", tempPassword);
        challengeResponses.put("NEW_PASSWORD", password);
        AdminRespondToAuthChallengeRequest finalRequest = new AdminRespondToAuthChallengeRequest()
                .withChallengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
                .withChallengeResponses(challengeResponses)
                .withClientId(awsConfig.getClientId())
                .withUserPoolId(awsConfig.getUserPoolId())
                .withSession(initialResponse.getSession());
        return awsConfig.awsCognitoIdentityProvider().adminRespondToAuthChallenge(finalRequest).getAuthenticationResult();
    }

    public ChangePasswordResult passwordReset(String oldPassword,String newPassword,String accessToken){
       try{ ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest();
        changePasswordRequest.setAccessToken(accessToken);
        changePasswordRequest.setPreviousPassword(oldPassword);
        changePasswordRequest.setProposedPassword(newPassword);
        return awsConfig.awsCognitoIdentityProvider().changePassword(changePasswordRequest);
    }catch (Exception e){
        throw e;
    }
    }
    public GetUserAttributeVerificationCodeResult getAttributeVerificationCode(final String attributeName, final String accessToken) {
       try{     GetUserAttributeVerificationCodeRequest getUserAttributeVerificationCodeRequest = new GetUserAttributeVerificationCodeRequest();
            getUserAttributeVerificationCodeRequest.setAccessToken(accessToken);
            getUserAttributeVerificationCodeRequest.setAttributeName(attributeName);
            return awsConfig.awsCognitoIdentityProvider().getUserAttributeVerificationCode(getUserAttributeVerificationCodeRequest);
    }catch (Exception e){
        throw e;
    }
    }

    public VerifyUserAttributeResult verifyAttribute(String attributeName, String verificationCode, String accessToken) {
    try{
            VerifyUserAttributeRequest verifyUserAttributeRequest = new VerifyUserAttributeRequest();
            verifyUserAttributeRequest.setAttributeName(attributeName);
            verifyUserAttributeRequest.setAccessToken(accessToken);
            verifyUserAttributeRequest.setCode(verificationCode);
            return awsConfig.awsCognitoIdentityProvider().verifyUserAttribute(verifyUserAttributeRequest);
    }catch (Exception e){
        throw e;
    }
    }

    public GetUserResult authorize(String accessToken) {
        try{
            GetUserRequest authRequest = new GetUserRequest().withAccessToken(accessToken);
            GetUserResult authResponse = awsConfig.awsCognitoIdentityProvider().getUser(authRequest);
            return authResponse;
        }catch (Exception e){
            throw e;
        }
    }

    public ResendConfirmationCodeResult resendConfirmation(String username) {
        try{
            ResendConfirmationCodeRequest resendConfirmationCodeRequest = new ResendConfirmationCodeRequest()
                    .withClientId(awsConfig.getClientId())
                    .withUsername(username);
            ResendConfirmationCodeResult resendConfirmationCodeResult = awsConfig.awsCognitoIdentityProvider().resendConfirmationCode(resendConfirmationCodeRequest);
            return resendConfirmationCodeResult;
        }catch (Exception e){
            throw e;
        }
    }

    public AdminGetUserResult adminGetUser(String username) {
        try{
            AdminGetUserRequest adminGetUserRequest = new AdminGetUserRequest()
                    .withUserPoolId(awsConfig.getUserPoolId())
                    .withUsername(username);
            AdminGetUserResult adminGetUserResult = awsConfig.awsCognitoIdentityProvider().adminGetUser(adminGetUserRequest);
            return adminGetUserResult;
        }catch (Exception e){
            throw e;
        }
    }

    public ForgotPasswordResult forgotPassword(String username){
        ForgotPasswordResult forgotPasswordResult = null;
        CodeDeliveryDetailsType codeDeliveryDetailsType = null;
        try {
            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest()
                    .withClientId(awsConfig.getClientId())
                    .withUsername(username);
            forgotPasswordResult =  awsConfig.awsCognitoIdentityProvider().forgotPassword(forgotPasswordRequest);
            codeDeliveryDetailsType = new CodeDeliveryDetailsType();
            codeDeliveryDetailsType.setDeliveryMedium("EMAIL");
            codeDeliveryDetailsType.setDeliveryMedium("SMS");
            forgotPasswordResult.setCodeDeliveryDetails(codeDeliveryDetailsType);
            }catch(Exception e) {
            throw e;
          //  System.out.println("Exception "+ e);
        }
        return forgotPasswordResult;
    }

    public ConfirmForgotPasswordResult confirmForgotPassword(String username,String password, String code){
        ConfirmForgotPasswordRequest forgotPasswordRequest=null;
        ConfirmForgotPasswordResult forgotPasswordResult=null;
        try {
            forgotPasswordRequest = new ConfirmForgotPasswordRequest()
                    .withClientId(awsConfig.getClientId())
                    .withUsername(username)
                    .withPassword(password)
                    .withConfirmationCode(code);
            forgotPasswordResult = awsConfig.awsCognitoIdentityProvider().confirmForgotPassword(forgotPasswordRequest);
        }catch(Exception e) {
            throw e;
        }
        return forgotPasswordResult;
    }

}
