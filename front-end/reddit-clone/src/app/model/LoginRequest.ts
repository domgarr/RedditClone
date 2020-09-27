export class LoginRequest {
    private username : string;
    private password : string;

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
        this.username = password;
    }
}