import React from "react";
import { Link, Outlet, useNavigate } from "react-router-dom";
import { authFetch } from "../utils/authFetch";

const AdminLayout = () => {
  const navigate = useNavigate();
  const email = localStorage.getItem("userEmail");

  React.useEffect(() => {
    if (!email) {
      navigate("/login");
      return;
    }

    authFetch(`/api/auth/user?email=${email}`)
      .then((res) => {
        if (!res.ok) throw new Error();
        return res.json();
      })
      .then((user) => {
        if (!user.admin) {
          navigate("/");
        }
      })
      .catch(() => navigate("/"));
  }, [email, navigate]);

  return (
    <div style={{ display: "flex" }}>
      <aside style={{ width: "200px", background: "#eee", padding: 10 }}>
        <h3 className="hover:text-amber-600 transition-colors"><Link to="/admin/products">Admin Dashboard</Link></h3>
        <ul style={{ listStyle: "none", padding: 0 }}>
          <li className="hover:text-amber-600 transition-colors  cursor-pointer"><Link to="/admin/products">Products</Link></li>
          <li className="hover:text-amber-600 transition-colors  cursor-pointer"><Link to="/admin/users">Users</Link></li>
          <li className="hover:text-amber-600 transition-colors"><Link to="/admin/orders">Orders</Link></li>
          <li className="hover:text-amber-600 transition-colors"><Link to="/admin/coupons">Coupons</Link></li>
          <li className="hover:text-amber-600 transition-colors"><Link to="/admin/carts">Carts</Link></li>
  
        <li className="hover:text-amber-600 transition-colors"><Link to="/">Bact To App Page</Link></li>
        </ul>
      </aside>
      <main style={{ flex: 1, padding: 20 }}>
        <Outlet />
      </main>
    </div>
  );
};

export default AdminLayout;
