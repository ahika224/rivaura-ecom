import { createContext, useContext, useEffect, useState } from 'react';
import AuthService from './AuthService';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext({
  auth: null,
  setAuth: () => {},
  user: null,
});

export const useAuth = () => useContext(AuthContext);

const AuthProvider = ({ children }) => {
  const [auth, setAuth] = useState(null);
  const currentUser = AuthService.getCurrentUser();
 const navigate = useNavigate();

  useEffect(() => {
   

if(currentUser!==null){
  setAuth(true);
}else{
  setAuth(false)
}

 
  }, [auth,currentUser,navigate]);

  return (
    <AuthContext.Provider value={{ auth, setAuth }}>
      {children}
    </AuthContext.Provider>
  );
};

export default AuthProvider;