import { useState, useEffect } from 'react'
import { NavLink } from 'react-router-dom'
import '../styles/Dashboard.css';

const NavIcon = ({ to, children, title }) => (
  <NavLink
    to={to}
    title={title}
    className={({ isActive }) => `dash-nav-icon${isActive ? ' active' : ''}`}
  >
    {children}
  </NavLink>
)

function Dashboard() {
  const [devices, setDevices] = useState([])
  const [layouts, setLayouts] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError]     = useState(null)

  useEffect(() => {
    Promise.all([
      fetch('/api/devices').then(r => r.json()),
      fetch('/api/layouts').then(r => r.json()),
    ])
      .then(([devRes, layRes]) => {
        setDevices(devRes.data ?? [])
        setLayouts(layRes.data ?? [])
      })
      .catch(() => setError('Could not reach the server. Is the backend running?'))
      .finally(() => setLoading(false))
  }, [])

  const online  = devices.filter(d => d.status === 'ONLINE').length
  const offline = devices.filter(d => d.status === 'OFFLINE').length
  const groupCount = new Set(devices.map(d => d.deviceGroupId).filter(Boolean)).size

  if (loading) return <div className="dashboard"><div className="dash-empty-full">Loading...</div></div>
  if (error)   return <div className="dashboard"><div className="dash-empty-full dash-empty-error">{error}</div></div>

  return (
    <div className="dashboard">

      {/* ── Sidebar ── */}
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

        <NavIcon to="/dashboard" title="Dashboard">
          <svg viewBox="0 0 16 16" fill="currentColor">
            <rect x="1" y="1" width="6" height="6" rx="1.2"/>
            <rect x="9" y="1" width="6" height="6" rx="1.2"/>
            <rect x="1" y="9" width="6" height="6" rx="1.2"/>
            <rect x="9" y="9" width="6" height="6" rx="1.2"/>
          </svg>
        </NavIcon>

        <NavIcon to="/devices" title="Devices">
          <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round">
            <rect x="2" y="3" width="12" height="2.5" rx="1"/>
            <rect x="2" y="6.75" width="8" height="2.5" rx="1"/>
            <rect x="2" y="10.5" width="10" height="2.5" rx="1"/>
          </svg>
        </NavIcon>

        <NavIcon to="/layouts" title="Layouts">
          <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round">
            <rect x="1.5" y="3" width="13" height="9" rx="1.5"/>
            <path d="M1.5 7h13M7 7v5" />
          </svg>
        </NavIcon>

        <NavIcon to="/content" title="Content">
          <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round" strokeLinejoin="round">
            <rect x="1.5" y="3" width="13" height="9" rx="1.5"/>
            <path d="M6.5 6.5l3 2-3 2V6.5z" fill="currentColor" stroke="none"/>
          </svg>
        </NavIcon>

        <div className="dash-nav-spacer" />

        <NavIcon to="/" title="Logout">
          <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.4" strokeLinecap="round">
            <circle cx="8" cy="5.5" r="2.5"/>
            <path d="M2.5 13.5c0-3 2.5-5 5.5-5s5.5 2 5.5 5"/>
          </svg>
        </NavIcon>
      </nav>

      {/* ── Main ── */}
      <div className="dash-main">

        {/* Top bar */}
        <div className="dash-topbar">
          <div className="dash-topbar-left">
            <div className="dash-topbar-title">Dashboard</div>
            <div className="dash-topbar-sub">
              {new Date().toLocaleDateString('en-US', { weekday: 'short', month: 'long', day: 'numeric' })}
            </div>
          </div>
          <div className="dash-topbar-right">
            <span className="pill pill-green">
              <span className="pill-dot" />
              {online} online
            </span>
            <button className="dash-add-btn">
              <svg width="9" height="9" viewBox="0 0 9 9" fill="none">
                <path d="M4.5 1v7M1 4.5h7" stroke="#fff" strokeWidth="1.5" strokeLinecap="round"/>
              </svg>
              Add device
            </button>
          </div>
        </div>

        {/* Body */}
        <div className="dash-body">

          {/* Stat cards */}
          <div className="dash-stats">
            <div className="dash-stat">
              <div className="dash-stat-label">Total devices</div>
              <div className="dash-stat-num">{devices.length}</div>
              <div className="dash-stat-foot">{groupCount} groups</div>
            </div>
            <div className="dash-stat">
              <div className="dash-stat-label">Online now</div>
              <div className="dash-stat-num green">{online}</div>
              <div className="dash-stat-foot red">{offline} offline</div>
            </div>
            <div className="dash-stat">
              <div className="dash-stat-label">Active layouts</div>
              <div className="dash-stat-num">{layouts.length}</div>
              <div className="dash-stat-foot">{groupCount} groups assigned</div>
            </div>
            <div className="dash-stat">
              <div className="dash-stat-label">Device groups</div>
              <div className="dash-stat-num">{groupCount}</div>
              <div className="dash-stat-foot">{devices.length} devices total</div>
            </div>
          </div>

          {/* Lower panels */}
          <div className="dash-lower">

            {/* Devices panel */}
            <div className="dash-panel">
              <div className="dash-panel-head">
                <span className="section-label">Devices</span>
                <NavLink to="/devices" className="dash-panel-action">View all →</NavLink>
              </div>
              <div className="dash-device-list">
                {devices.length === 0 ? (
                  <div className="dash-empty-panel">No devices registered yet.</div>
                ) : (
                  devices.slice(0, 5).map(d => (
                    <div key={d.id} className="dash-device-item">
                      <div className={`dash-device-dot ${d.status === 'ONLINE' ? 'green' : 'red'}`} />
                      <div className="dash-device-info">
                        <div className="dash-device-name">{d.name}</div>
                        <div className="dash-device-group">{d.ipAddress ?? 'No IP assigned'}</div>
                      </div>
                      <span className={`pill ${d.status === 'ONLINE' ? 'pill-green' : 'pill-red'}`}>
                        {d.status === 'ONLINE' ? 'online' : 'offline'}
                      </span>
                    </div>
                  ))
                )}
              </div>
            </div>

            {/* Quick actions panel */}
            <div className="dash-panel">
              <div className="dash-panel-head">
                <span className="section-label">Quick actions</span>
              </div>
              <div className="dash-actions-grid">
                <NavLink to="/devices" className="dash-action-tile">
                  <div className="dash-action-icon">
                    <svg viewBox="0 0 12 12"><path d="M6 1v10M1 6h10"/></svg>
                  </div>
                  <div className="dash-action-label">Add device</div>
                  <div className="dash-action-sub">Register a screen</div>
                </NavLink>
                <NavLink to="/layouts" className="dash-action-tile">
                  <div className="dash-action-icon">
                    <svg viewBox="0 0 12 12"><rect x="1" y="1" width="10" height="10" rx="1"/><path d="M1 5h10M5 5v6"/></svg>
                  </div>
                  <div className="dash-action-label">Create layout</div>
                  <div className="dash-action-sub">Build a grid</div>
                </NavLink>
                <NavLink to="/content" className="dash-action-tile">
                  <div className="dash-action-icon">
                    <svg viewBox="0 0 12 12"><path d="M6 1.5v6M3.5 4L6 1.5 8.5 4"/><path d="M2 9.5h8"/></svg>
                  </div>
                  <div className="dash-action-label">Upload content</div>
                  <div className="dash-action-sub">Images &amp; videos</div>
                </NavLink>
                <div className="dash-action-tile">
                  <div className="dash-action-icon">
                    <svg viewBox="0 0 12 12"><rect x="1" y="2.5" width="10" height="7" rx="1"/><path d="M4.5 5.5l3 2-3 2V5.5z" strokeWidth="0" fill="#555"/></svg>
                  </div>
                  <div className="dash-action-label">Preview display</div>
                  <div className="dash-action-sub">Live layout view</div>
                </div>
              </div>
              <div className="dash-action-tile wide">
                <div className="dash-action-icon">
                  <svg viewBox="0 0 12 12"><circle cx="6" cy="6" r="4.5"/><path d="M6 4v3l2 1"/></svg>
                </div>
                <div>
                  <div className="dash-action-label">Manage groups</div>
                  <div className="dash-action-sub">Assign devices to layouts</div>
                </div>
              </div>
            </div>

            {/* Layout preview panel */}
            <div className="dash-panel">
              <div className="dash-panel-head">
                <span className="section-label">Layout preview</span>
                <NavLink to="/layouts" className="dash-panel-action">Edit →</NavLink>
              </div>
              <div className="dash-preview-body">
                {layouts.length === 0 ? (
                  <div className="dash-empty-panel">No layouts created yet.</div>
                ) : (
                  <>
                    <div className="dash-preview-screen">
                      <div
                        className="dash-preview-grid"
                        style={{
                          gridTemplateColumns: `repeat(${layouts[0].cols ?? 2}, 1fr)`,
                          gridTemplateRows: `repeat(${layouts[0].rows ?? 1}, 1fr)`,
                        }}
                      >
                        {Array.from({ length: (layouts[0].cols ?? 2) * (layouts[0].rows ?? 1) }).map((_, i) => (
                          <div key={i} className="dash-preview-slot">
                            {layouts[0].slots?.[i] ? 'module' : ''}
                          </div>
                        ))}
                      </div>
                    </div>
                    <div className="dash-preview-meta">
                      <span className="dash-preview-name">{layouts[0].name}</span>
                      <span className="dash-preview-info">{layouts[0].cols} cols · {layouts[0].rows} rows</span>
                    </div>
                    <div className="dash-coll-list">
                      {layouts.slice(1).map(l => (
                        <div key={l.id} className="dash-coll-item">
                          <div className="dash-coll-thumb" />
                          <span className="dash-coll-name">{l.name}</span>
                          <span className="dash-coll-count">{l.cols}×{l.rows}</span>
                        </div>
                      ))}
                    </div>
                  </>
                )}
              </div>
            </div>

          </div>
        </div>
      </div>
    </div>
  )
}

export default Dashboard