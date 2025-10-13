import React, { useState, useEffect, useRef } from "react";
import { ShoppingBag, Heart, Search, Menu, X, Phone } from "lucide-react";
import { useAuth } from "../utils/AuthContext";
import AuthService from "../utils/AuthService";
import { authFetch } from "../utils/authFetch";

const Navbar = ({ handleSearchSubmit, searchInput, setSearchInput, searchResults, viewProduct, handleSearchClick, setSearchOpen, searchOpen }) => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const searchRef = useRef(null);
   const currentUser = AuthService.getCurrentUser();
const { auth,setAuth } = useAuth();
  const [allCategories, setAllCategories] = useState([]);

const logOut = () => {
     
    AuthService.logout();
    setAuth(false);
    window.location.reload();
  //navigate("/login")
    
  };
    useEffect(() => {
  const getAllCategories = async () => {  
  
      try {     
        const res = await authFetch('/api/products/categories');  
        if (res.ok) {     
          const data = await res.json();
          setAllCategories(data);
        } else {  
          throw new Error('Failed to fetch categories');
        }
      } catch (err) {
        console.error('Error fetching categories:', err);
        setAllCategories([]);
  
      }
  
    };
    getAllCategories();
  }, []);
    
  
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (searchRef.current && !searchRef.current.contains(event.target)) {
        setSearchOpen(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => document.removeEventListener("mousedown", handleClickOutside);
  }, [setSearchOpen]);

  return (
    <nav className="bg-white border-b border-gray-100 sticky top-0 z-50 backdrop-blur-sm bg-white/95">
      <div className="max-w-7xl mx-auto px-6">
        {/* Top Bar */}

        {/* Main Navigation */}
        <div className="flex items-center justify-between py-4" >
            <div className="">
            <a href="/" className="text-3xl font-light tracking-[0.3em] text-gray-800 hover:text-amber-600 transition-colors">
             <b> <i>AHIKA</i></b>
            </a>
          </div>
          

          {/* Logo */}
        

          <div className="flex items-center space-x-6" >
            <div className=" md:flex items-center space-x-4" hidden={mobileMenuOpen}> 
              <div className="w-full md:w-96">
              <div
                  ref={searchRef}
                  className=" absolute top-3  w-full md:w-96 bg-white shadow-2xl border border-gray-200 rounded-lg z-50"
                >
                  <form onSubmit={handleSearchSubmit} className="relative">
                    <input
                      type="text"
                      value={searchInput}
                      onChange={(e) => setSearchInput(e.target.value)}
                      placeholder="Search for jewelry..."
                      className="w-full pl-10 pr-4 py-3 border-b border-gray-200 focus:outline-none focus:ring-2 focus:ring-[#EBD6FB] text-gray-700 rounded-t-lg"
                    />
                    <Search className="w-5 h-5 text-gray-500 absolute left-3 top-1/2 transform -translate-y-1/2" />
                  </form>
                  {searchResults.length > 0 && (
                    <div className="max-h-80 overflow-y-auto">
                      {searchResults.map((product) => (
                        <div
                          key={product.id || product._id}
                          className="p-4 border-b border-gray-100 hover:bg-gray-50 cursor-pointer"
                          onClick={() => viewProduct(product.id || product._id)}
                        >
                          <div className="flex items-center space-x-4">
                            <img
                              src={product.image || product.imageUrl || "https://images.unsplash.com/photo-1605100804763-247f67b3557e?ixlib=rb-4.0.3&auto=format&fit=crop&w=800&q=80"}
                              alt={product.name}
                              className="w-16 h-16 object-cover rounded"
                            />
                            <div>
                              <p className="text-sm font-light text-gray-900">{product.name}</p>
                              <p className="text-xs text-gray-500">Weight: {product.weight}g</p>
                              <p className="text-xs text-gray-500">{product.category}</p>
                              <p className="text-sm font-medium text-gray-900">${product.price?.toLocaleString()} <span className="text-xs text-gray-500">per gram</span> </p>
                            </div>
                          </div>
                        </div>
                      ))}
                    </div>
                  )}
                  {searchInput.trim() !== "" && searchResults.length === 0 && (
                    <div className="  p-4 text-center text-gray-500 text-sm">
                    No products found.
                  </div>
                   )}
                   {searchInput.trim() !== "" && searchResults.length >0 && (
                   <div className="  p-4 border-t border-gray-200">
                      <button
                        onClick={handleSearchClick}
                        className="w-full text-sm text-gray-700 hover:text-amber-600 transition-colors text-center"
                      >
                        View all results
                      </button>
                    </div>
                  )}
                </div>
                </div>
                {!auth && ( 
               <div className="flex items-center text-sm text-gray-600 hover:text-amber-600 transition-colors" >
            <Heart className="w-4 h-4 mr-2 text-amber-600" />
            Register to get updates!
          </div>)}
          {!auth && (
              <><a href="/login" className="text-sm text-gray-600 hover:text-amber-600 transition-colors">Login</a><a href="/signup" className="text-sm text-gray-600 hover:text-amber-600 transition-colors">Signup</a></>
          )}
          {auth && (    
              <><span className="text-sm text-gray-600 hover:text-amber-600 transition-colors">Hi, {currentUser?.firstName || currentUser?.username}</span>
            {currentUser?.admin && (  <a href="/admin" className="text-sm text-gray-600 hover:text-amber-600 transition-colors">Admin Dashboard</a>)}
              <button className="text-sm text-gray-600 hover:text-amber-600 transition-colors block" onClick={logOut}>Logout</button></>
          )}
           {auth && (  <a href="/cart" className="flex text-sm text-gray-600 hover:text-amber-600 transition-colors">
                <ShoppingBag className="w-5 h-5  hover:text-amber-600 cursor-pointer transition-colors" />Cart
              </a>)}
              </div>
              
            <div className="flex items-center space-x-4 relative">
          
              <button
                onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
                className="md:hidden"
              >
                {mobileMenuOpen ? <X className="w-5 h-5" /> : <Menu className="w-5 h-5" />}
              </button>
            
            </div>
          </div>
        </div>

        {/* Navigation Links */}
        <div className="hidden md:flex items-center justify-center space-x-12 py-4 border-t border-gray-50" hidden={mobileMenuOpen}>
           
          <a href="/" className="text-gray-700 hover:text-amber-600 transition-colors font-medium">HOME</a>
          {allCategories.map((category) => (
            <a key={category} href={`/collections/${category}`} className="text-gray-700 hover:text-amber-600 transition-colors font-medium">
              {category.toUpperCase()}
            </a>
          ))}
         
          <a href="/account" className="text-gray-700 hover:text-amber-600 transition-colors font-medium">ABOUT</a>
        </div>

        {/* Mobile Menu */}
        {mobileMenuOpen && 
 auth && (    
              <span className="text-sm text-gray-600 hover:text-amber-600 transition-colors">Hi, {currentUser?.firstName || currentUser?.username}</span>
          )   &&  (
              <><a href="/login" className="text-sm text-gray-600 hover:text-amber-600 transition-colors">Login</a><a href="/signup" className="text-sm text-gray-600 hover:text-amber-600 transition-colors">Signup</a>
  
          <div className="md:hidden border-t border-gray-50 py-1">
            <div className="flex flex-col space-y-1">
              <a href="/" className="text-gray-700 hover:text-amber-600 transition-colors font-medium">Home</a>
              {allCategories.map((category) => (
            <a key={category} href={`/collections/${category}`} className="text-gray-700 hover:text-amber-600 transition-colors font-medium">
              {category.toUpperCase()}
            </a>
          ))}
           
             {auth && ( <a href="/cart" className="flex text-gray-700 hover:text-amber-600 transition-colors font-medium">         
                     <ShoppingBag className="w-5 h-5  hover:text-amber-600 cursor-pointer transition-colors" />Cart
</a>)}
              <a href="/account" className="text-gray-700 hover:text-amber-600 transition-colors font-medium">Account</a>
              <a href="/contact" className="text-gray-700 hover:text-amber-600 transition-colors font-medium">Contact</a>
              {!auth && (
              <div className="pt-4 border-t border-gray-100">
                <a href="/login" className="text-gray-700 hover:text-amber-600 transition-colors font-medium">Login</a>
                <a href="/signup" className="text-gray-700 hover:text-amber-600 transition-colors font-medium">Signup</a>
              </div>
)}              {auth && (
              <div className="pt-4 border-t border-gray-100">

                <button className="text-gray-700 hover:text-amber-600 transition-colors font-medium" onClick={logOut}>Logout</button>
              </div>
)}
            </div>
          </div>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;