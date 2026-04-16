import { useState, useEffect } from 'react';
import '../styles/Layouts.css';

function Layouts() {
  const [layouts, setLayouts] = useState([]);
  const [selected, setSelected] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showNewForm, setShowNewForm] = useState(false);
  const [newLayout, setNewLayout] = useState({ name: '', cols: 2, rows: 2 });

  useEffect(() => {
    fetchLayouts();
  }, []);

  const fetchLayouts = () => {
    setLoading(true);
    fetch('/api/layouts')
      .then(r => r.json())
      .then(res => {
        setLayouts(res.data ?? []);
        if (res.data?.length > 0) setSelected(res.data[0]);
      })
      .catch(() => setError('Could not reach the server.'))
      .finally(() => setLoading(false));
  };

  const handleDelete = (id) => {
    fetch(`/api/layouts/${id}`, { method: 'DELETE' })
      .then(() => {
        const updated = layouts.filter(l => l.id !== id);
        setLayouts(updated);
        setSelected(updated[0] ?? null);
      });
  };

  const handleCreate = () => {
    fetch('/api/layouts', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(newLayout),
    })
      .then(r => r.json())
      .then(res => {
        if (res.data) {
          setLayouts(prev => [...prev, res.data]);
          setSelected(res.data);
          setShowNewForm(false);
          setNewLayout({ name: '', cols: 2, rows: 2 });
        }
      });
  };

  if (loading) return <div className="layouts-page"><div className="layouts-empty">Loading...</div></div>;
  if (error)   return <div className="layouts-page"><div className="layouts-empty">{error}</div></div>;

  return (
    <div className="layouts-page">
      <div className="layouts-header">
        <div>
          <h1 className="layouts-title">Layout Editor</h1>
          <p className="layouts-sub">Design custom layouts for your displays</p>
        </div>
      </div>

      {/* Layout list */}
      <div className="layouts-card">
        <div className="layouts-card-head">
          <span className="layouts-card-title">Layouts</span>
          <button className="layouts-new-btn" onClick={() => setShowNewForm(true)}>
            + New Layout
          </button>
        </div>

        {showNewForm && (
          <div className="layouts-new-form">
            <input
              className="layouts-input"
              placeholder="Layout name"
              value={newLayout.name}
              onChange={e => setNewLayout(p => ({ ...p, name: e.target.value }))}
            />
            <input
              className="layouts-input small"
              type="number"
              placeholder="Cols"
              value={newLayout.cols}
              onChange={e => setNewLayout(p => ({ ...p, cols: Number(e.target.value) }))}
            />
            <input
              className="layouts-input small"
              type="number"
              placeholder="Rows"
              value={newLayout.rows}
              onChange={e => setNewLayout(p => ({ ...p, rows: Number(e.target.value) }))}
            />
            <button className="layouts-save-btn" onClick={handleCreate}>Create</button>
            <button className="layouts-cancel-btn" onClick={() => setShowNewForm(false)}>Cancel</button>
          </div>
        )}

        <div className="layouts-list">
          {layouts.length === 0 ? (
            <div className="layouts-empty">No layouts yet.</div>
          ) : (
            layouts.map(l => (
              <div
                key={l.id}
                className={`layouts-item ${selected?.id === l.id ? 'active' : ''}`}
                onClick={() => setSelected(l)}
              >
                <div>
                  <div className="layouts-item-name">{l.name}</div>
                  <div className="layouts-item-meta">{l.cols}×{l.rows} grid</div>
                </div>
                <div className="layouts-item-actions">
                  <button className="layouts-icon-btn" onClick={e => { e.stopPropagation(); setSelected(l); }}>✏️</button>
                  <button className="layouts-icon-btn danger" onClick={e => { e.stopPropagation(); handleDelete(l.id); }}>🗑</button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>

      {/* Layout preview */}
      {selected && (
        <div className="layouts-card">
          <div className="layouts-card-head">
            <span className="layouts-card-title">{selected.name}</span>
            <button className="layouts-save-btn">💾 Save</button>
          </div>
          <div className="layouts-preview">
            <div
              className="layouts-grid"
              style={{
                gridTemplateColumns: `repeat(${selected.cols}, 1fr)`,
                gridTemplateRows: `repeat(${selected.rows}, 1fr)`,
              }}
            >
              {Array.from({ length: selected.cols * selected.rows }).map((_, i) => (
                <div key={i} className="layouts-slot">
                  {selected.slots?.[i] ? 'module' : ''}
                </div>
              ))}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default Layouts;