/*
My Getter/Setters will be capitilized.
*/
export class SignUpRequest{
    private username : string = "";
    private password : string = "";
    private email: string = "";

    get Username() : string {
        return this.username;
    }

    set Username(username : string) {
        this.username = username;
    }

    get Password() : string {
        return this.password;
    }

    set Password(password : string) {
        this.password = password;
    }

    get Email() : string {
        return this.email;
    }

    set Email(email : string) {
        this.email = email;
    }
}