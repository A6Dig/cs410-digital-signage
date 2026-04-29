import Section from "./Section";

// ===== LayoutRenderer — renders sections according to the selected layout =====

function LayoutRenderer({ layout, slots, selectedSectionId, onSelectSection, rows, cols, gridSlots }) {
  const getSlotKey = (slot) => slot.id ?? `${slot.rowPos}-${slot.colPos}`;

  return (
      <div
          className="layout-grid-test"
          style={{
            gridTemplateColumns: `repeat(${cols}, 1fr)`,
            gridTemplateRows: `repeat(${rows}, 1fr)`,
          }}
        >
          {slots.map((slot, index) => {
            const gridInfo = gridSlots[index];
            const gridStyle = gridInfo ? {
              gridColumn: `${gridInfo.colPos} / span ${gridInfo.colSpan}`,
              gridRow: `${gridInfo.rowPos} / span ${gridInfo.rowSpan}`,
            } : {};

            return (
              <Section
                key={getSlotKey(slot)}
                section={slot}
                slotKey={getSlotKey(slot)}
                isSelected={getSlotKey(slot) === selectedSectionId}
                onSelect={onSelectSection}
                gridStyle={gridStyle}
              />
            );
          })}
      </div>
    );

//   switch (layout) {
//     // Full-width single section
//     case "single":
//       return (
//         <div className="layout-single">
//           {sections.map(renderSection)}
//         </div>
//       );
//
//     // Two equal columns side by side
//     case "two-columns":
//       return (
//         <div className="layout-two-columns">
//           {sections.map(renderSection)}
//         </div>
//       );
//
//     // Full-width header row + two columns below
//     case "header-two-columns":
//       return (
//         <div className="layout-header-two-columns">
//           <div className="header-row">
//             {sections[0] && renderSection(sections[0])}
//           </div>
//           <div className="columns-row">
//             {sections[1] && renderSection(sections[1])}
//             {sections[2] && renderSection(sections[2])}
//           </div>
//         </div>
//       );
//
//     // 2×2 grid
//     case "grid":
//       return (
//         <div className="layout-grid">
//           {sections.map(renderSection)}
//         </div>
//       );
//
//     // Large left section + 3 stacked right sections (65/35)
//     case "right-column":
//       return (
//         <div className="layout-right-column">
//           <div className="right-col-left">
//             {sections[0] && renderSection(sections[0])}
//           </div>
//           <div className="right-col-right">
//             {sections[1] && renderSection(sections[1])}
//             {sections[2] && renderSection(sections[2])}
//             {sections[3] && renderSection(sections[3])}
//           </div>
//         </div>
//       );
//
//     // Large top section + 3 bottom sections in a row (65/35)
//     case "bottom-row":
//       return (
//         <div className="layout-bottom-row">
//           <div className="bottom-row-top">
//             {sections[0] && renderSection(sections[0])}
//           </div>
//           <div className="bottom-row-bottom">
//             {sections[1] && renderSection(sections[1])}
//             {sections[2] && renderSection(sections[2])}
//             {sections[3] && renderSection(sections[3])}
//           </div>
//         </div>
//       );
//
//     // 6-section grid: large top-left, 2 stacked right, 3 bottom
//     case "six-section-grid":
//       return (
//         <div className="layout-six-section-grid">
//           {sections[0] && <div className="six-topleft">{renderSection(sections[0])}</div>}
//           {sections[1] && <div className="six-topright">{renderSection(sections[1])}</div>}
//           {sections[2] && <div className="six-midright">{renderSection(sections[2])}</div>}
//           {sections[3] && <div className="six-botleft">{renderSection(sections[3])}</div>}
//           {sections[4] && <div className="six-botmid">{renderSection(sections[4])}</div>}
//           {sections[5] && <div className="six-botright">{renderSection(sections[5])}</div>}
//         </div>
//       );
//
//     default:
//       return (
//         <div className="layout-single">
//           {sections.map(renderSection)}
//         </div>
//       );
//   }
}

export default LayoutRenderer;
