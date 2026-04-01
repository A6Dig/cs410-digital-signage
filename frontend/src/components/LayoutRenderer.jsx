import Section from "./Section";

// ===== LayoutRenderer — renders sections according to the selected layout =====

function LayoutRenderer({ layout, sections, selectedSectionId, onSelectSection }) {
  const renderSection = (section) => (
    <Section
      key={section.id}
      section={section}
      isSelected={section.id === selectedSectionId}
      onSelect={onSelectSection}
    />
  );

  switch (layout) {
    // Full-width single section
    case "single":
      return (
        <div className="layout-single">
          {sections.map(renderSection)}
        </div>
      );

    // Two equal columns side by side
    case "two-columns":
      return (
        <div className="layout-two-columns">
          {sections.map(renderSection)}
        </div>
      );

    // Full-width header row + two columns below
    case "header-two-columns":
      return (
        <div className="layout-header-two-columns">
          <div className="header-row">
            {sections[0] && renderSection(sections[0])}
          </div>
          <div className="columns-row">
            {sections[1] && renderSection(sections[1])}
            {sections[2] && renderSection(sections[2])}
          </div>
        </div>
      );

    // 2×2 grid
    case "grid":
      return (
        <div className="layout-grid">
          {sections.map(renderSection)}
        </div>
      );

    default:
      return (
        <div className="layout-single">
          {sections.map(renderSection)}
        </div>
      );
  }
}

export default LayoutRenderer;
