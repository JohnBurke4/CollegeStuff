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

    componentDidMount() {
        console.log("mount")
        this.drawChart();
    }

    drawChart() {

        let xDomain = Array.from(Array(this.props.data.length).keys());
        //let data = Array.from({length: 24}, (_, i) => i + 1);

        const data = this.props.data;
        console.log(Math.max(...data));
        const margin = this.props.margin;
        const width = this.props.width - 4 * margin;
        const height = this.props.height - 4 * margin;
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
        
        .attr('x', (actual, index, array) => xScale(index))
        .attr('y', (s) => yScale(s))
        .attr('height', (s) => height - yScale(s))
        .attr('width', xScale.bandwidth())
        //.attr('fill', 'green')

        // Adding in the axis labels
        chart.append("text")
        .attr("transform",
            "translate(" + (width/2) + " ," + 
                           (height + 40) + ")")
        .style("text-anchor", "middle")
        .text("Hour of the Day");

        chart.append("text")
        .attr("transform", "rotate(-90)")
        .attr("y", 0 - margin)
        .attr("x",0 - (height / 2))
        .attr("dy", "1em")
        .style("text-anchor", "middle")
        .text("Number of Commits"); 


        }

    render(){
        return <div className="text-center">
            <h3>Commits per time of day</h3>
            <svg height={this.props.height} /*className="bg-primary"*/ width={this.props.width} ></svg>
            </div>
    }
}

export default BarChart;