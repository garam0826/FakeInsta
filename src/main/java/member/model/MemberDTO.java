package member.model;

public class MemberDTO {
    private String id;
    private String pw;
    private String pwCheck;
    private String name;
    private String gender;
    private String birth;
    private String phone;
    private String email;
    private String address;
    private String addressDetail;

    public void setId(String id){
        this.id=id;
    }
    public String getId(){
        return this.id;
    }
    public void setPw(String pw){
        this.pw=pw;
    }
    public String getPw(){
        return this.pw;
    }

    public void setPwCheck(String pwCheck){
        this.pwCheck=pwCheck;
    }
    public String getPwCheck(){
        return this.pwCheck;
    }

    public void setName(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

    public void setGender(String gender){
        this.gender=gender;
    }
    public String getGender(){
        return this.gender;
    }
    public void setBirth(String birth){
        this.birth=birth;
    }
    public String getBirth(){
        return this.birth=birth;
    }
    public void setPhone(String phone){
        this.phone=phone;
    }
    public String getPhone(){
        return this.phone=phone;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setAddress(String address){
        this.address=address;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddressDetail(String addressDetail){
        this.addressDetail=addressDetail;
    }
    public String getAddressDetail(){
        return this.addressDetail;
    }







}
