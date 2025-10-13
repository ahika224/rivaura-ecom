
const API_URL = "/api/auth";

class AuthService{

 register = (email, password, username, profilePic ,phone) => {
  return fetch('/api/auth/signup', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json'
         },
        body: JSON.stringify({ email, password, username, profilePic ,phone}),
      });
};

 login = async (email, password) => {
  
  const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });
      
   if (response.ok) {
    const data = await response.json();
    localStorage.setItem("token", data.jwt);
     localStorage.setItem("user", JSON.stringify(data.user));
     localStorage.setItem("userEmail",data.user.email);
   }
   
   return response.ok;
   
};

 logout = () => {
  localStorage.removeItem("user");
  
 
};

 getCurrentUser = () => {
  return JSON.parse(localStorage.getItem("user"));
};


updatePassword =(email, password)=>{
  return fetch("/api/auth/reset-password", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });
}


}
const instance = new AuthService();
export default instance;
