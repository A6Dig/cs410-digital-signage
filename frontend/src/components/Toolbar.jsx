const LAYOUTS = ["single", "two-columns", "header-two-columns", "grid", "right-column", "bottom-row", "six-section-grid"];

function Toolbar({ currentLayout, onAddSlide, onChangeLayout, onPreview, onGoToLayouts, onSave, saveStatus }) {
  return (
    <div className="toolbar">
      <span className="toolbar-title">Digital Signage Editor</span>

      <button onClick={onAddSlide}>+ Add Slide</button>

      <select
        value={currentLayout}
        onChange={(e) => onChangeLayout(e.target.value)}
      >
        {LAYOUTS.map((layout) => (
          <option key={layout} value={layout}>
            {layout}
          </option>
        ))}
      </select>

      <button onClick={onPreview}>Preview</button>

      <button onClick={onSave} disabled={saveStatus === "saving"}>
        {saveStatus === "saving"
          ? "Saving..."
          : saveStatus === "saved"
            ? "Saved!"
            : saveStatus === "error"
              ? "Save failed"
              : "Save"}
      </button>

      <button onClick={onGoToLayouts}>Layouts</button>
    </div>
  );
}

export default Toolbar;
