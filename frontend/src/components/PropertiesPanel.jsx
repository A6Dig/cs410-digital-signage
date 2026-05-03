// ===== PropertiesPanel — right sidebar for editing selected section =====

const CONTENT_TYPES = ["text", "image", "video", "slideshow", "weather"];

function PropertiesPanel({ section, onUpdate }) {
  if (!section) {
    return (
      <div className="properties-panel">
        <h3>Properties</h3>
        <p className="properties-empty">Select a section to edit its properties.</p>
      </div>
    );
  }

  // FUTURE: Auth token will be attached to save requests here
  // const token = localStorage.getItem("token");

  const handleTypeChange = (e) => {
    // Reset content when switching types
    onUpdate(section.id, { contentType: e.target.value, content: "" });
  };

  const handleContentChange = (e) => {
    onUpdate(section.id, { content: e.target.value });
  };

  return (
    <div className="properties-panel">
      <h3>Properties — Section {section.id}</h3>

      {/* Content type selector */}
      <label htmlFor="content-type">Content Type</label>
      <select
        id="content-type"
        value={section.contentType}
        onChange={handleTypeChange}
      >
        {CONTENT_TYPES.map((type) => (
          <option key={type} value={type}>
            {type}
          </option>
        ))}
      </select>

      {/* Dynamic input based on content type */}
      {section.contentType === "text" && (
        <>
          <label htmlFor="text-content">Text Content</label>
          <textarea
            id="text-content"
            value={section.content}
            onChange={handleContentChange}
            placeholder="Enter text..."
          />
          <label htmlFor="font-size">Font Size</label>
          <div className="font-size-control">
            <button
              onClick={() =>
                onUpdate(section.id, {
                  fontSize: Math.max(8, (section.fontSize || 16) - 2),
                })
              }
            >
              -
            </button>
            <input
              id="font-size"
              type="number"
              min="8"
              max="120"
              value={section.fontSize || 16}
              onChange={(e) => {
                const v = parseInt(e.target.value, 10);
                if (!isNaN(v) && v >= 8 && v <= 120)
                  onUpdate(section.id, { fontSize: v });
              }}
            />
            <span className="font-size-unit">px</span>
            <button
              onClick={() =>
                onUpdate(section.id, {
                  fontSize: Math.min(120, (section.fontSize || 16) + 2),
                })
              }
            >
              +
            </button>
          </div>
        </>
      )}

      {section.contentType === "image" && (
        <>
          <label htmlFor="image-file">Upload Image</label>
          <input
            id="image-file"
            type="file"
            accept="image/*"
            onChange={(e) => {
              const file = e.target.files[0];
              if (file) {
                const localUrl = URL.createObjectURL(file);
                onUpdate(section.id, { content: localUrl });
              }
            }}
          />
        </>
      )}

      {section.contentType === "video" && (
        <>
          <label htmlFor="video-url">Video / Embed URL</label>
          <input
            id="video-url"
            type="text"
            value={section.content}
            onChange={handleContentChange}
            placeholder="https://www.youtube.com/embed/..."
          />
        </>
      )}

      {section.contentType === "slideshow" && (
        <p className="properties-empty">
          Slideshow cycles through placeholder images automatically.
        </p>
      )}

      {section.contentType === "weather" && (
        <p className="properties-empty">
          Weather widget shows mock data. Replace with a live API later.
        </p>
      )}
    </div>
  );
}

export default PropertiesPanel;
