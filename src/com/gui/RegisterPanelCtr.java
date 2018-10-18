package com.gui;

import ATMServer.TransObject;
import com.MainApp;
import com.accountType.*;
import com.exceptionType.RegisterException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.TreeMap;

public class RegisterPanelCtr {
    @FXML
    private Button Btn_submit;
    @FXML
    private Button Btn_backward;
    @FXML
    private TextField usernameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private PasswordField repasswordText;
    @FXML
    private TextField idnumText;
    @FXML
    private TextField emailText;
    @FXML
    private TextArea adressText;
    @FXML
    private ChoiceBox selectBox;
    @FXML
    private Text statusText;

    //客户端连接
    public Socket client=null;
    private ObjectInputStream ois=null;
    private ObjectOutputStream oos=null;

    public ChoiceBox getSelectBox() {
        return selectBox;
    }

    public void setSelectBox(ChoiceBox selectBox) {
        this.selectBox = selectBox;
    }

    public void submition() throws IOException{
        String accountType=(String) selectBox.getValue();
        String username=usernameText.getText();
        String password=passwordText.getText();
        String repassword=repasswordText.getText();
        String idnum=idnumText.getText();
        String email=emailText.getText();
        String adress=adressText.getText();
        if (accountType.equals("") || username.equals("") || password.equals("") || repassword.equals("") ||
                idnum.equals("") || email.equals("")|| adress.equals("")){
            statusText.setText("有信息未填写！");
        }else if (!password.equals(repassword)){
            statusText.setText("密码不一致");
        }else {
            client=new Socket("127.0.0.1",20006);
            oos=new ObjectOutputStream(client.getOutputStream());
            ois=new ObjectInputStream(client.getInputStream());

            TransObject object=new TransObject("注册");
            object.setFromAccountType(accountType);
            object.setFromName(username);
            object.setFromPassword(password);
            object.setFromIdNum(idnum);
            object.setFromEmail(email);
            object.setFromAdress(adress);
            oos.writeObject(object);
            try{
                TransObject getObject=(TransObject)ois.readObject();
                if (!getObject.getFromName().equals("null")){  //表示添加成功
                    selectBox.getSelectionModel().selectFirst();
                    usernameText.clear();
                    passwordText.clear();
                    repasswordText.clear();
                    idnumText.clear();
                    emailText.clear();
                    adressText.clear();
                    statusText.setText("账户添加成功！");
                }else{
                    statusText.setText("账号已存在！");
                }
            }catch (ClassNotFoundException e){
                e.printStackTrace();
            }
            client.close();
        }

    }
    public void backward(){
        try {
            MainApp.initMainPanel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
