/*
 * Copyright (c) 2021 Kaiserpfalz EDV-Service, Roland T. Lichti.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

const path = require('path');
const TsconfigPathsPlugin = require('tsconfig-paths-webpack-plugin');
const appConfig = require('../webpack.common');

module.exports = ({ config, mode }) => {
  config.module.rules = [];
  config.module.rules.push(...appConfig(mode).module.rules);
  config.module.rules.push({
    test: /\.css$/,
    include: [
      path.resolve(__dirname, '../src'),
      path.resolve(__dirname, '../node_modules/@storybook'),
      path.resolve(__dirname, '../node_modules/patternfly'),
      path.resolve(__dirname, '../node_modules/@patternfly/patternfly'),
      path.resolve(__dirname, '../node_modules/@patternfly/react-styles/css'),
      path.resolve(__dirname, '../node_modules/@patternfly/react-core/dist/styles/base.css'),
      path.resolve(__dirname, '../node_modules/@patternfly/react-core/dist/esm/@patternfly/patternfly'),
      path.resolve(__dirname, '../node_modules/@patternfly/react-core/node_modules/@patternfly/react-styles/css'),
      path.resolve(__dirname, '../node_modules/@patternfly/react-table/node_modules/@patternfly/react-styles/css'),
      path.resolve(__dirname, '../node_modules/@patternfly/react-inline-edit-extension/node_modules/@patternfly/react-styles/css')
    ],
    use: ["style-loader", "css-loader"]
  });
  config.module.rules.push({
    test: /\.tsx?$/,
    include: path.resolve(__dirname, '../src'),
    use: [
      require.resolve('react-docgen-typescript-loader'),
    ],
  })
  config.resolve.plugins = [
    new TsconfigPathsPlugin({
      configFile: path.resolve(__dirname, "../tsconfig.json")
    })
  ];
  config.resolve.extensions.push('.ts', '.tsx');
  return config;
};
