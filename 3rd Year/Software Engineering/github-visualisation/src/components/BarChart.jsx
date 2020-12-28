import React, {Component} from 'react';
import * as d3 from "d3";

class BarChart extends Component {
    constructor(props){
        super(props);
        this.state = {
            data: this.props.data
        }
        
    }


    componentDidUpdate() {
        this.drawChart();
    }

    drawChart() {
        const data = this.props.data;
        console.log(data);
            const svg = d3.select("svg")
            .attr("width", 800)
            .attr("height", 800);
            
            svg.selectAll("rect")
            .data(data)
            .enter()
            .append("rect")
            .attr("x", (d, i) => i * 30)
            .attr("y", (d, i) => 800 - 5 * d)
            .attr("width", 25)
            .attr("height", (d, i) => d * 10)
            .attr("fill", "green");
        

        
    }

    render(){
        return <svg id="Graph"></svg>
    }
}

export default BarChart;