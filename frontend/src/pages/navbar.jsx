import { NavLink } from 'react-router-dom';
import './Navbar.css';

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="logo">MyBrand</div>
      <ul className="nav-links">
        <li><NavLink to="/" end>Home</NavLink></li>
        <li><NavLink to="'./pages/Content'">About</NavLink></li>
        <li><NavLink to="/services">Services</NavLink></li>
      </ul>
    </nav>
  );
}

export default Navbar;
