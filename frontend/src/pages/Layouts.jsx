import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Layouts.css';

function Layouts() {
  const [layouts, setLayouts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showNewForm, setShowNewForm] = useState(false);
  const [newName, setNewName] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    fetch('/api/layouts')
      .then(r => r.json())
      .then(res => setLayouts(res.data ?? []))
      .catch(() => setError('Could not reach the server.'))
      .finally(() => setLoading(false));
  }, []);

  const handleCreate = () => {
    if (!newName.trim()) return;
    fetch('/api/layouts', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: newName, cols: 2, rows: 2 }),
    })
      .then(r => r.json())
      .then(res => {
        if (res.data) navigate(`/canvas?layoutId=${res.data.id}`);
      });
  };

  const handleDelete = (e, id) => {
    e.stopPropagation();
    fetch(`/api/layouts/${id}`, { method: 'DELETE' })
      .then(() => setLayouts(prev => prev.filter(l => l.id !== id)));
  };

  if (loading) return <div className="layouts-page"><p className="layouts-empty">Loading...</p></div>;
  if (error)   return <div className="layouts-page"><p className="layouts-empty">{error}</p></div>;

  return (
    <div className="layouts-page">

      <div className="layouts-topbar">
        <div>
          <h1 className="layouts-title">Layouts</h1>
          <p className="layouts-sub">Design custom layouts for your displays</p>
        </div>
        <button className="layouts-new-btn" onClick={() => setShowNewForm(v => !v)}>
          + New Layout
        </button>
      </div>

      {showNewForm && (
        <div className="layouts-form-bar">
          <input
            className="layouts-input"
            placeholder="Layout name"
            value={newName}
            onChange={e => setNewName(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && handleCreate()}
          />
          <button className="layouts-save-btn" onClick={handleCreate}>Create & Open</button>
          <button className="layouts-cancel-btn" onClick={() => setShowNewForm(false)}>Cancel</button>
        </div>
      )}

      <div className="layouts-section">
        <p className="layouts-section-label">Previously created</p>
        {layouts.length === 0 ? (
          <p className="layouts-empty">No layouts yet. Create one above.</p>
        ) : (
          <div className="layouts-grid">
            {layouts.map(l => (
              <div key={l.id} className="layouts-card">
                <div className="layouts-card-preview" onClick={() => navigate(`/canvas?layoutId=${l.id}`)}>
                  <div
                    className="layouts-card-grid"
                    style={{
                      gridTemplateColumns: `repeat(${l.cols}, 1fr)`,
                      gridTemplateRows: `repeat(${l.rows}, 1fr)`,
                    }}
                  >
                    {Array.from({ length: l.cols * l.rows }).map((_, i) => (
                      <div key={i} className="layouts-card-slot" />
                    ))}
                  </div>
                </div>
                <div className="layouts-card-footer">
                  <div>
                    <div className="layouts-card-name">{l.name.includes('::') ? l.name.split('::')[0].replace(/\[\d+\]$/, '') : l.name}</div>
                    {l.slots && l.slots.length > 0 && (
                      <div style={{ fontSize: '0.7rem', color: '#888', marginTop: 2 }}>
                        {new Set(l.slots.map(s => Math.floor((s.rowPos - 1) / 100))).size} slide(s)
                      </div>
                    )}
                  </div>
                  <div className="layouts-card-actions">
                    <button className="layouts-action-btn" onClick={() => navigate(`/canvas?layoutId=${l.id}`)}>
                      <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M11.5 2.5l2 2L5 13H3v-2L11.5 2.5z"/>
                      </svg>
                    </button>
                    <button className="layouts-action-btn danger" onClick={e => handleDelete(e, l.id)}>
                      <svg viewBox="0 0 16 16" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
                        <path d="M3 5h10M6 5V3h4v2M6 8v4M10 8v4"/>
                        <rect x="4" y="5" width="8" height="8" rx="1"/>
                      </svg>
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>

    </div>
  );
}

export default Layouts;