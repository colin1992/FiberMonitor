package domain;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
    /**
	 * 序列号
	 * **/
	@Entity
	@Table(name="SERIAL_CODE")
	public class SerialCode {
		@Id
		@GenericGenerator(name="key_increment",strategy="increment")
		@GeneratedValue(generator="key_increment")
		@Column(name="ID",nullable=false,unique=true,updatable=false)
		private Long id;			//标识
		@Column(name="CODE")
		private String code;			//验证码
		
		@Column(name="MAC")
		private String MAC;			//服务器MAC
		
		@Column(name="VALIDATION")
		private String validation;			//验证码,用户输入的未经过MD5加密
		
		public Long getId(){
			return this.id;
		}
		//code
		public void setSerialCode(String code){
			this.code=code;
		}
		public String getSerialCode(){
			return this.code;
		}
		//code
		public void setValidation(String validation){
			this.validation=validation;
		}
		public String getValidation(){
			return this.validation;
		}
		//MAC
		public void setMAC(String MAC){
			this.MAC=MAC;
		}
		public String getMAC(){
			return this.MAC;
		}
}
