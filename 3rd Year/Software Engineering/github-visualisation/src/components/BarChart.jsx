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
        console.log("update")
        this.drawChart();
    }

    drawChart() {

        let xDomain = Array.from({length: 24}, (_, i) => i + 1);
        //let data = Array.from({length: 24}, (_, i) => i + 1);

        const data = this.props.data;
        console.log(Math.max(...data));
        const margin = this.props.margin;
        const width = this.props.width - 2 * margin;
        const height = this.props.height - 2 * margin;
        const svg = d3.select("svg");

        const chart = svg.append('g')
        .attr('transform', `translate(${margin}, ${margin})`);

        const yScale = d3.scaleLinear()
        .range([height, 0])
        .domain([0, Math.max(...data)]);

        chart.append('g')
        .call(d3.axisLeft(yScale));

        const xScale = d3.scaleBand()
        .range([0, width])
        .domain(xDomain.map(x => x))
        .padding(0.2)

        chart.append('g')
        .attr('transform', `translate(0, ${height})`)
        .call(d3.axisBottom(xScale));
        
        chart.selectAll()
        .data(data)
        .enter()
        .append('rect')
        
        .attr('x', (actual, index, array) => xScale(1+index))
        .attr('y', (s) => yScale(s))
        .attr('height', (s) => height - yScale(s))
        .attr('width', xScale.bandwidth())
        }

    render(){
        return <svg id="1" height={this.props.height} width={this.props.width}></svg>
    }
}

export default BarChart;