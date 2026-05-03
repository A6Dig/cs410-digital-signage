import { NavLink, useLocation } from 'react-router-dom';
import '../styles/Navbar.css';

const NavIcon = ({ to, children, title, label }) => (
  <NavLink
    to={to}
    title={title}
    className={({ isActive }) => `dash-nav-icon${isActive ? ' active' : ''}`}
  >
    {children}
    <span className="dash-nav-label">{label}</span>
  </NavLink>
);

const Navbar = () => {
  const location = useLocation();
  if (location.pathname === '/') return null;

  return (
    <nav className="dash-nav">
      <div className="dash-nav-logo">
        <svg width="14" height="14" viewBox="0 0 14 14" fill="none">
          <rect x="1" y="1" width="5" height="5" rx="1" fill="#111"/>
          <rect x="8" y="1" width="5" height="5" rx="1" fill="#111"/>
          <rect x="1" y="8" width="5" height="5" rx="1" fill="#111"/>
          <rect x="8" y="8" width="3" height="2" rx="0.8" fill="#111"/>
          <rect x="11" y="8" width="2" height="5" rx="0.8" fill="#111"/>
        </svg>
      </div>

    <div className="dash-nav-brand">
      <span className="dash-nav-brand-title">Digital Signage</span>
      <span className="dash-nav-brand-sub">Admin Panel</span>
    </div>
      
      <NavIcon to="/dashboard" title="Dashboard" label="Dashboard">
        <svg viewBox="0 0 16 16" fill="currentColor">
          <rect x="1" y="1" width="6" height="6" rx="1.2"/>
          <rect x="9" y="1" width="6" height="6" rx="1.2"/>
          <rect x="1" y="9" width="6" height="6" rx="1.2"/>
          <rect x="9" y="9" width="6" height="6" rx="1.2"/>
        </svg>
      </NavIcon>

      <NavIcon to="/devices" title="Devices" label="Devices">
        <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round">
          <rect x="2" y="3" width="12" height="2.5" rx="1"/>
          <rect x="2" y="6.75" width="8" height="2.5" rx="1"/>
          <rect x="2" y="10.5" width="10" height="2.5" rx="1"/>
        </svg>
      </NavIcon>

      <NavIcon to="/layouts" title="Layouts" label="Layouts">
        <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round">
          <rect x="1.5" y="3" width="13" height="9" rx="1.5"/>
          <path d="M1.5 7h13M7 7v5" />
        </svg>
      </NavIcon>

      <NavIcon to="/content" title="Content" label="Content">
        <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round" strokeLinejoin="round">
          <rect x="1.5" y="3" width="13" height="9" rx="1.5"/>
          <path d="M6.5 6.5l3 2-3 2V6.5z" fill="currentColor" stroke="none"/>
        </svg>
      </NavIcon>

      <div className="dash-nav-spacer" />

      <NavIcon to="/" title="Logout" label="Logout">
        <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round">
          <circle cx="8" cy="5.5" r="2.5"/>
          <path d="M2.5 13.5c0-3 2.5-5 5.5-5s5.5 2 5.5 5"/>
        </svg>
      </NavIcon>
    </nav>
  );
};

export default Navbar;