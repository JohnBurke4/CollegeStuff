import React, {
    Component
} from 'react';
import * as d3 from "d3";
import * as collection from "d3-collection"

class LolipopChart extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: this.props.data
        }

    }


    componentDidUpdate() {
        console.log("update")
        this.drawChart();
    }

    componentDidMount() {
        console.log("mount")
        this.drawChart();
    }

    drawChart() {

        //let data = Array.from({length: 24}, (_, i) => i + 1);

        let data = collection.entries(this.props.data).sort((a,b) => (a.value > b.value) ? -1 : 1);
        if (data.length > 30){
            data = data.slice(0, 49);
        }
        let max = Math.max.apply(Math, data.map(function(o) { return o.value; }))
        console.log(data)
        const margin = this.props.margin;
        const width = this.props.width - 4*margin;
        const height = this.props.height - 4*margin;
        const svg = d3.select("#lolipop")
        .append("svg")
        .attr("height", height + 5*margin)
        .attr("width", width + 4*margin)
        .append("g")
        .attr("transform",
          "translate(" + 2*margin + "," + margin + ")");;

        // X axis
        var x = d3.scaleBand()
            .range([0, width])
            .domain(data.map(function(d) {
                return d.key;
            }))
            .padding(1);
        svg.append("g")
            .attr("transform", "translate(0," + height + ")")
            .call(d3.axisBottom(x))
            .selectAll("text")
            .attr("transform", "translate(-10,0)rotate(-45)")
            .style("text-anchor", "end");

        // Add Y axis
        var y = d3.scaleLinear()
            .domain([0, max])
            .range([height, 0]);
        svg.append("g")
            .call(d3.axisLeft(y));

        // Lines
        svg.selectAll("myline")
            .data(data)
            .enter()
            .append("line")
            .attr("x1", function(d) {
                return x(d.key);
            })
            .attr("x2", function(d) {
                return x(d.key);
            })
            .attr("y1", function(d) {
                return y(d.value);
            })
            .attr("y2", y(0))
            .attr("stroke", "grey")

        // Circles
        svg.selectAll("mycircle")
            .data(data)
            .enter()
            .append("circle")
            .attr("cx", function(d) {
                return x(d.key);
            })
            .attr("cy", function(d) {
                return y(d.value);
            })
            .attr("r", "4")
            .style("fill", "#69b3a2")
            .attr("stroke", "black")

        svg.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin*2)
        .attr("x",0 - (height / 2))
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .text("Number of Commits"); 
    }

    render() {
        return <div className = "text-center" id="lolipop" >
            <h3>Top 50 committers</h3>
            </div>
    }
}

export default LolipopChart;