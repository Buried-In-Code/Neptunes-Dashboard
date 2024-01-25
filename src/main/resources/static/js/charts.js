const backgroundColours = [
  "rgba(255,0,0,0.5)",
  "rgba(255,215,0,0.5)",
  "rgba(0,128,0,0.5)",
  "rgba(0,191,255,0.5)",
  "rgba(128,0,128,0.5)",
  "rgba(128,128,128,0.5)",
  "rgba(160,82,45,0.5)",
  pattern.draw("plus", "rgba(255,0,0,0.5)"),
  pattern.draw("cross", "rgba(255,215,0,0.5)"),
  pattern.draw("dash", "rgba(0,128,0,0.5)"),
  pattern.draw("cross-dash", "rgba(0,191,255,0.5)"),
  pattern.draw("dot", "rgba(128,0,128,0.5)"),
  pattern.draw("dot-dash", "rgba(128,128,128,0.5)"),
  pattern.draw("disc", "rgba(160,82,45,0.5)"),
  pattern.draw("ring", "rgba(255,0,0,0.5)"),
  pattern.draw("line", "rgba(255,215,0,0.5)"),
  pattern.draw("line-vertical", "rgba(0,128,0,0.5)"),
  pattern.draw("weave", "rgba(0,191,255,0.5)"),
  pattern.draw("zigzag", "rgba(128,0,128,0.5)"),
  pattern.draw("zigzag-vertical", "rgba(128,128,128,0.5)"),
  pattern.draw("diagonal", "rgba(160,82,45,0.5)"),
  pattern.draw("diagonal-right-left", "rgba(255,0,0,0.5)"),
  pattern.draw("square", "rgba(255,215,0,0.5)"),
  pattern.draw("box", "rgba(0,128,0,0.5)"),
  pattern.draw("triangle", "rgba(0,191,255,0.5)"),
  pattern.draw("triangle-inverted", "rgba(128,0,128,0.5)"),
  pattern.draw("diamond", "rgba(128,128,128,0.5)"),
  pattern.draw("diamond-box", "rgba(160,82,45,0.5)")
];
const borderColours = [
  "rgba(255,0,0,1)",
  "rgba(255,215,0,1)",
  "rgba(0,128,0,1)",
  "rgba(0,191,255,1)",
  "rgba(128,0,128,1)",
  "rgba(128,128,128,1)",
  "rgba(160,82,45,1)",
  "rgba(255,0,0,1)",
  "rgba(255,215,0,1)",
  "rgba(0,128,0,1)",
  "rgba(0,191,255,1)",
  "rgba(128,0,128,1)",
  "rgba(128,128,128,1)",
  "rgba(160,82,45,1)",
  "rgba(255,0,0,1)",
  "rgba(255,215,0,1)",
  "rgba(0,128,0,1)",
  "rgba(0,191,255,1)",
  "rgba(128,0,128,1)",
  "rgba(128,128,128,1)",
  "rgba(160,82,45,1)",
  "rgba(255,0,0,1)",
  "rgba(255,215,0,1)",
  "rgba(0,128,0,1)",
  "rgba(0,191,255,1)",
  "rgba(128,0,128,1)",
  "rgba(128,128,128,1)",
  "rgba(160,82,45,1)"
];


function toggleChartLoading(id) {
  const progress = document.getElementById(`${id}-loading`).parentElement;
  const chart = document.getElementById(id).parentElement;
  progress.hidden = !progress.hidden;
  chart.hidden = !chart.hidden;
}

function createPieChart(elementId, labels, data) {
  const config = {
    type: "doughnut",
    data: {
      labels: labels,
      datasets: [{
        data: data,
        backgroundColor: backgroundColours,
        borderColor: borderColours,
        borderWidth: 2,
        hoverOffset: 25,
      }],
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
    },
  };
  new Chart(document.getElementById(elementId), config);
  toggleChartLoading(elementId);
}

function createDataset(index, data, label, type, yAxisID = "y") {
  return {
    backgroundColor: backgroundColours[index],
    borderColor: borderColours[index],
    borderWidth: 2,
    borderSkipped: false,
    data: data,
    label: label,
    type: type,
    yAxisID: yAxisID
  }
}

function createChart(elementId, labels, datasets) {
  const config = {
    data: {
      labels: labels,
      datasets: datasets,
    },
    options: {
      interaction: {
        intersect: false,
        mode: "nearest",
      },
      plugins: {
        legend: {
          display: datasets.length > 1
        },
      },
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        x: {
          title: {
            display: false,
          },
          position: "bottom",
        },
        y: {
          title: {
            display: false,
          },
          position: "left",
          beginAtZero: false,
        }
      }
    }
  };
  new Chart(document.getElementById(elementId), config);
  toggleChartLoading(elementId);
}
