import { AbstractControl, ValidatorFn } from "@angular/forms";

export function passwordEqualityValidator(password : string) : ValidatorFn {
    console.log("username: " + password);
    return (control : AbstractControl): {[key:string]: any} | null =>{
        console.log(control.parent);
        //console.log(control.parent.controls.keys['password']);
        
        let password;
        try{
            password = control.parent.controls["password"];
        }catch(e){
            password = "";
        }

        console.log(control.parent.controls); 
      const equality : boolean = password === control.value;
      console.log(equality);
      return equality ? {passwordEqualityError: {value: "PASSWORDS DO NOT MATCH"}}: null;
    };
  }