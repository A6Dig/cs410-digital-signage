// ===== Toolbar — top bar with editor actions =====

const LAYOUTS = ["single", "two-columns", "header-two-columns", "grid", "right-column", "bottom-row", "six-section-grid"];

function Toolbar({ currentLayout, onAddSlide, onChangeLayout, onPreview, onLogout,
                                             onAddRow, onAddColumn, onRemoveRow, onRemoveColumn, onSave }) {
  return (
    <div className="toolbar">
      <span className="toolbar-title">Digital Signage Editor</span>

      {/* Add a new slide */}
{/*       <button onClick={onAddSlide}>+ Add Slide</button> */}
      <button onClick={onRemoveColumn}>-</button>
      <span>Column</span>
      <button onClick={onAddColumn}>+</button>

      <button onClick={onRemoveRow}>-</button>
      <span>Row</span>
      <button onClick={onAddRow}>+</button>

      <button onClick={onSave}>Save Layout</button>

      {/* Layout selector */}
{/*       <select */}
{/*         value={currentLayout} */}
{/*         onChange={(e) => onChangeLayout(e.target.value)} */}
{/*       > */}
{/*         {LAYOUTS.map((layout) => ( */}
{/*           <option key={layout} value={layout}> */}
{/*             {layout} */}
{/*           </option> */}
{/*         ))} */}
{/*       </select> */}

      {/* Preview — logs slides to console for now */}
      {/* FUTURE: Open a full-screen preview modal or route */}
      <button onClick={onPreview}>Preview</button>

      {/* Logout */}
      <button onClick={onLogout}>Logout</button>
    </div>
  );
}

export default Toolbar;
